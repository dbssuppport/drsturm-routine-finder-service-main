package com.e2x.bigcommerce.routinefinder.cli;

import com.e2x.bigcommerce.routinefinder.cli.reporting.LoggerRoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.cli.utils.ScenarioCsvOutputRunner;
import com.e2x.bigcommerce.routinefinder.cli.verification.*;
import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.data.configuration.RoutineFinderDataRepositoryConfiguration;
import com.e2x.bigcommerce.routinefinder.data.repository.RoutineGraphReaderWriter;
import com.e2x.bigcommerce.routinefinder.data.repository.RoutineGraphUriRepository;
import com.e2x.bigcommerce.routinefinder.data.repository.bucket.GcpBucketRoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.DelegatingRoutineGraphImportInterop;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.ValidatingRoutineGraphImportInterop;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.validator.RoutineGraphVerticesValidationFactory;
import com.google.api.client.util.Lists;
import com.google.cloud.storage.StorageOptions;

import java.nio.file.Path;
import java.util.List;

import static com.e2x.bigcommerce.routinefinder.data.configuration.RoutineFinderDataRepositoryConfiguration.*;

public class RoutineFinderCliConfiguration {

    public static RoutineGraphVertexImporter graphImporter(String bucketName, Boolean force) {
        var errorReporting = new LoggerRoutineGraphErrorReporting();
        var conditionExpressionImportValidator = conditionExpressionImportValidator();

        var validator = RoutineGraphVerticesValidationFactory
                .newValidator()
                .with(errorReporting)
                .with(inMemoryQuestionNodeRepository())
                .with(inMemoryOptionValueNodeRepository())
                .with(inMemorySkuNodeRepository())
                .with(conditionExpressionImportValidator)
                .instantiate();

        var graphRepository = gcpRepository(bucketName);

        return new RoutineGraphVertexImporter(new ValidatingRoutineGraphImportInterop(errorReporting, validator), errorReporting, graphRepository, force);
    }

    public static List<ScenarioRunner> scenarioRunners(String routineFinderServiceUrl, Boolean outputCsv, String outputFile) {
        List<ScenarioRunner> runners = Lists.newArrayList();
        runners.add(new RoutineFinderServiceScenarioRunner(new RoutineFinderClient(routineFinderServiceUrl)));

        if (outputCsv) {
            runners.add(new ScenarioCsvOutputRunner(RoutineFinderDataRepositoryConfiguration.inMemoryQuestionDefinitionRepository(), outputFile));
        }

        return runners;
    }

    public static ConditionExpressionImportValidator conditionExpressionImportValidator() {
        return new ConditionExpressionImportValidator(RoutineFinderDataRepositoryConfiguration.inMemoryQuestionDefinitionRepository(), RoutineFinderDataRepositoryConfiguration.inMemoryOptionDefinitionRepository());
    }

    public static RoutineGraphVertexImporter graphReader(RoutineGraphRepository routineGraphRepository) {
        var errorReporting = new LoggerRoutineGraphErrorReporting();
        var routineGraph = new InMemoryRoutineGraph();
        var importer = new DelegatingRoutineGraphImportInterop(routineGraph);

        return new RoutineGraphVertexImporter(importer, errorReporting, routineGraphRepository, false);
    }

    public static RoutineGraphRepository gcpRepository(String bucketName) {
        return new GcpBucketRoutineGraphRepository(bucketName, StorageOptions.getDefaultInstance(), routineGraphReaderWriter());
    }

    public static ScenarioFactory scenarioFactory() {
        return new ScenarioFactory(RoutineFinderDataRepositoryConfiguration.skuDefinitionRepository());
    }

    private static RoutineGraphRepository fileRepository(String filename) {
        return new RoutineGraphUriRepository(Path.of(filename).toUri(), RoutineFinderDataRepositoryConfiguration.graphMLReaderWriter());
    }

    private static RoutineGraphReaderWriter routineGraphReaderWriter() {
        return RoutineFinderDataRepositoryConfiguration.graphMLReaderWriter();
    }

}
