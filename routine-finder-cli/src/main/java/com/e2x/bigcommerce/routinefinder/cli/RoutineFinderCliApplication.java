package com.e2x.bigcommerce.routinefinder.cli;

import com.e2x.bigcommerce.routinefinder.cli.command.RoutineFinderCliRootCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class RoutineFinderCliApplication implements CommandLineRunner, ExitCodeGenerator {

    private final CommandLine.IFactory factory;
    private final RoutineFinderCliRootCommand routineFinderCliRootCommand;
    private int exitCode;

    RoutineFinderCliApplication(CommandLine.IFactory factory, RoutineFinderCliRootCommand routineFinderCliRootCommand) {
        this.factory = factory;
        this.routineFinderCliRootCommand = routineFinderCliRootCommand;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(routineFinderCliRootCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return this.exitCode;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(RoutineFinderCliApplication.class, args)));
    }
}
