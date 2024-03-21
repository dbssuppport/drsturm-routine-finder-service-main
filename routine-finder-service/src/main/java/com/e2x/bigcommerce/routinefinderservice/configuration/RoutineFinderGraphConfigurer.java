package com.e2x.bigcommerce.routinefinderservice.configuration;

import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.data.SkuDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.configuration.RoutineFinderDataRepositoryConfiguration;
import com.e2x.bigcommerce.routinefinder.data.repository.RoutineGraphReaderWriter;
import com.e2x.bigcommerce.routinefinder.data.repository.bucket.GcpBucketRoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.data.repository.graph.GraphMLReaderWriter;
import com.e2x.bigcommerce.routinefinder.enquiry.QuestionnaireRoutineGraphService;
import com.e2x.bigcommerce.routinefinderservice.service.QuestionDefinitionService;
import com.e2x.bigcommerce.routinefinderservice.service.RepositoryAwareQuestionDefinitionService;
import com.e2x.bigcommerce.routinefinderservice.service.RoutineEnquiryService;
import com.e2x.bigcommerce.routinefinderservice.service.routine.GraphRoutineService;
import com.e2x.bigcommerce.routinefinderservice.service.routine.QuestionnaireMapper;
import com.e2x.bigcommerce.routinefinderservice.service.routine.RoutineEnquiryMapper;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Configuration
public class RoutineFinderGraphConfigurer {

    @Bean
    public GraphMLReaderWriter graphMLReaderWriter() {
        return new GraphMLReaderWriter();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.cloud.gcp.storage.enabled", matchIfMissing = true, havingValue = "false")
    public RoutineGraphRepository routineGraphRepository() throws IOException {
        log.warn("test graph loaded...");

        var inputStream = Objects.requireNonNull(new ClassPathResource("test-graph.gml").getInputStream());
        var graphReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return RoutineFinderDataRepositoryConfiguration.testGraphRoutineRepository(graphReader);
    }

    @Bean
    @ConditionalOnProperty(value = "spring.cloud.gcp.storage.enabled", matchIfMissing = true)
    public RoutineGraphRepository gcpBucketRoutineGraphRepository(RoutineGraphReaderWriter routineGraphReaderWriter, RoutineFinderServiceConfiguration routineFinderServiceConfiguration) {
        log.info("GCP bucket routine graph repository initialised.");

        return new GcpBucketRoutineGraphRepository(routineFinderServiceConfiguration.getBucketName(), StorageOptions.getDefaultInstance(), routineGraphReaderWriter);
    }

    @Bean
    public RoutineEnquiryService routineEnquiryService(RoutineEnquiryMapper routineEnquiryMapper, RoutineGraphRepository routineGraphRepository, QuestionnaireMapper questionnaireMapper) {
        var questionnaireRoutineGraphService = new QuestionnaireRoutineGraphService();

        return new GraphRoutineService(routineGraphRepository, questionnaireRoutineGraphService, routineEnquiryMapper, questionnaireMapper);
    }

    @Bean
    public QuestionDefinitionRepository questionDefinitionRepository() {
        return RoutineFinderDataRepositoryConfiguration.storeAwareQuestionDefinitionRepository();
    }

    @Bean
    public OptionDefinitionRepository optionDefinitionRepository() {
        return RoutineFinderDataRepositoryConfiguration.storeAwareOptionDefinitionRepository();
    }

    @Bean
    public SkuDefinitionRepository skuDefinitionRepository() {
        return RoutineFinderDataRepositoryConfiguration.skuDefinitionRepository();
    }

    @Bean
    public QuestionDefinitionService questionDefinitionService(OptionDefinitionRepository optionDefinitionRepository, QuestionDefinitionRepository questionDefinitionRepository) {
        return new RepositoryAwareQuestionDefinitionService(questionDefinitionRepository, optionDefinitionRepository);
    }

}
