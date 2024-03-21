package com.e2x.bigcommerce.routinefinder.cli.command;

import com.e2x.bigcommerce.routinefinder.cli.RoutineFinderCliConfiguration;
import com.e2x.bigcommerce.routinefinder.cli.RoutineGraphConfigurationReader;
import com.e2x.bigcommerce.routinefinder.cli.mindmup.MindMupJsonRoutineGraphConfigurationImporter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Parameters;

@Component
@Command(name = "import", mixinStandardHelpOptions = true, version = "import 1.0", description = "validate and import data for routine finder")
public class ImportCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "file to be imported")
    private File file;

    @Parameters(index = "1", description = "bucket name")
    private String bucketName;

    @Parameters(index = "2", description = "publish regardless of errors found", defaultValue = "false")
    private Boolean force = false;

    private final RoutineGraphConfigurationReader routineGraphConfigurationReader;

    public ImportCommand(MindMupJsonRoutineGraphConfigurationImporter routineGraphConfigurationReader) {
        this.routineGraphConfigurationReader = routineGraphConfigurationReader;
    }

    @Override
    public Integer call() throws Exception {
        var vertexImporter = RoutineFinderCliConfiguration.graphImporter(bucketName, force);

        var reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        routineGraphConfigurationReader.read(reader, vertexImporter);

        return 0;
    }

}
