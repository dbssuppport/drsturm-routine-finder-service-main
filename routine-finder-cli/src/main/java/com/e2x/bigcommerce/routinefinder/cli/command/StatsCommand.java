package com.e2x.bigcommerce.routinefinder.cli.command;

import com.e2x.bigcommerce.routinefinder.cli.RoutineFinderCliConfiguration;
import com.e2x.bigcommerce.routinefinder.cli.mindmup.MindMupJsonRoutineGraphConfigurationImporter;
import com.e2x.bigcommerce.routinefinder.cli.reporting.RoutineGraphStatisticCompiler;
import com.e2x.bigcommerce.routinefinder.data.repository.InMemoryRoutineGraphRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Parameters;

@Slf4j
@Component
@Command(name = "stats", mixinStandardHelpOptions = true, version = "stats 1.0", description = "output various stats on the graph to import")
public class StatsCommand implements Callable<Integer> {

    private final MindMupJsonRoutineGraphConfigurationImporter importer;

    @Parameters(index = "0", description = "file to be calculate stats from")
    private File file;

    public StatsCommand(MindMupJsonRoutineGraphConfigurationImporter mindMupJsonRoutineGraphConfigurationImporter) {
        importer = mindMupJsonRoutineGraphConfigurationImporter;
    }

    @Override
    public Integer call() throws Exception {
        var repository = new InMemoryRoutineGraphRepository();
        var graphReader = RoutineFinderCliConfiguration.graphReader(repository);

        var fileReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        importer.read(fileReader, graphReader);

        var importedGraph = repository.fetchCurrent();

        var stats = new RoutineGraphStatisticCompiler(importedGraph);

        log.info(String.format("routine count: %s", stats.getRoutineCount()));
        log.info(String.format("shortest number of questions asked: %s", stats.getShortestPathLength()));
        log.info(String.format("longest number of questions asked: %s", stats.getLongestPathLength()));
        log.info(String.format("average number of questions asked: %s", new DecimalFormat("#.00").format(stats.getAveragePathLength())));

        return 0;

    }
}
