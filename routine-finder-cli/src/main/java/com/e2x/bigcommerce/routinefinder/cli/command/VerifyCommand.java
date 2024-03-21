package com.e2x.bigcommerce.routinefinder.cli.command;

import com.e2x.bigcommerce.routinefinder.cli.RoutineFinderCliConfiguration;
import com.e2x.bigcommerce.routinefinder.cli.verification.RoutineGraphScenarioCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Parameters;

@Slf4j
@Component
@Command(name = "verify", mixinStandardHelpOptions = true, version = "scenario 1.0", description = "verify routines from a list of scenarios")
public class VerifyCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "bucket name containing the stored graph")
    private String bucketName;

    @Parameters(index = "1", description = "URL to routine finder service to verify")
    private String routineFinderServiceUrl;

    @Parameters(index = "2", description = "outputs a csv of scenario")
    private Boolean outputCsv = false;

    @Parameters(index = "3", description = "output file name")
    private String fileName = "scenario.csv";

    @Override
    public Integer call() throws Exception {
        var routineGraphRepository = RoutineFinderCliConfiguration.gcpRepository(bucketName);
        var scenarioFactory = RoutineFinderCliConfiguration.scenarioFactory();

        var importedGraph = routineGraphRepository.fetchCurrent();

        var scenarioCalculator = new RoutineGraphScenarioCalculator(scenarioFactory);
        var scenarioRunners = RoutineFinderCliConfiguration.scenarioRunners(routineFinderServiceUrl, outputCsv, fileName);
        var index = new AtomicInteger(0);

        scenarioCalculator
                .calculateScenarios(importedGraph)
                .forEach(s -> {
                    log.info(String.format("running scenario %s: %s", index.incrementAndGet(), s.toString()));
                    scenarioRunners.forEach(r -> r.execute(s));
                });

        return 0;
    }

}
