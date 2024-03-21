package com.e2x.bigcommerce.routinefinder.data.configuration;

import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.SkuDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.repository.*;
import com.e2x.bigcommerce.routinefinder.data.repository.graph.GraphMLReaderWriter;
import com.e2x.bigcommerce.routinefinder.data.resource.PropertiesResourceRepositoriesDataReader;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.OptionValueNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.QuestionNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.SkuNodeRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.Reader;

@Slf4j
public class RoutineFinderDataRepositoryConfiguration {

    public static QuestionDefinitionRepository inMemoryQuestionDefinitionRepository() {
        var repository = new InMemoryQuestionDefinitionRepository();

        var repositoryLoader = new PropertiesResourceRepositoriesDataReader();
        repositoryLoader.load(repository, "question-definition.properties");

        return repository;
    }

    public static QuestionDefinitionRepository storeAwareQuestionDefinitionRepository() {
        return new StoreAwareQuestionDefinitionRepository();
    }

    public static OptionDefinitionRepository inMemoryOptionDefinitionRepository() {
        var repository = new InMemoryOptionDefinitionRepository();

        var repositoryLoader = new PropertiesResourceRepositoriesDataReader();
        repositoryLoader.load(repository, "question-definition.properties");

        return repository;
    }

    public static OptionDefinitionRepository storeAwareOptionDefinitionRepository() {
        return new StoreAwareOptionDefinitionRepository();
    }

    public static OptionValueNodeRepository inMemoryOptionValueNodeRepository() {
        var repository = new InMemoryOptionDefinitionRepository();

        var repositoryLoader = new PropertiesResourceRepositoriesDataReader();
        repositoryLoader.load(repository, "question-definition.properties");

        return repository;
    }

    public static QuestionNodeRepository inMemoryQuestionNodeRepository() {
        var repository = new InMemoryQuestionDefinitionRepository();

        var repositoryLoader = new PropertiesResourceRepositoriesDataReader();
        repositoryLoader.load(repository, "question-definition.properties");

        return repository;
    }

    public static SkuNodeRepository inMemorySkuNodeRepository() {
        var repository = new InMemorySkuDefinitionRepository();

        var repositoryLoader = new PropertiesResourceRepositoriesDataReader();
        repositoryLoader.load(repository, "sku-definition.properties");

        return repository;
    }

    public static SkuDefinitionRepository skuDefinitionRepository() {
        return new StoreAwareSkuDefinitionRepository();
    }

    public static GraphMLReaderWriter graphMLReaderWriter() {
        return new GraphMLReaderWriter();
    }

    public static InMemoryRoutineGraphRepository testGraphRoutineRepository(Reader reader) {
        var graph = graphMLReaderWriter().read(reader);
        return new InMemoryRoutineGraphRepository(graph);
    }

}
