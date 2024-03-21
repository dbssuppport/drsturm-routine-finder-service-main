package com.e2x.bigcommerce.routinefinder.cli.command;

import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;

@Component
@Command(name = "rfs", subcommands = { ImportCommand.class, StatsCommand.class, VerifyCommand.class })
public class RoutineFinderCliRootCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
