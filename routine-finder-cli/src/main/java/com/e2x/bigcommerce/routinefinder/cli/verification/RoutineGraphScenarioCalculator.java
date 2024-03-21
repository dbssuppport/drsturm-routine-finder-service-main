package com.e2x.bigcommerce.routinefinder.cli.verification;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RoutineGraphScenarioCalculator {

    private final ScenarioFactory scenarioFactory;

    public RoutineGraphScenarioCalculator(ScenarioFactory scenarioFactory) {
        this.scenarioFactory = scenarioFactory;
    }

    public Stream<Scenario> calculateScenarios(RoutineGraph routineGraph) {
        return RoutineGraphStatsHelper
                .routinesIn(routineGraph)
                .flatMap(v -> pathsToRootFor(v, routineGraph))
                .map(path -> scenarioFactory.createScenarioFor(path, routineGraph));
    }

    private Stream<RoutineGraphPath<Vertex, Edge>> pathsToRootFor(Vertex vertex, RoutineGraph routineGraph) {
        var pathToRootIterator = new GraphPathToRootIterator<>(vertex, routineGraph);
        Spliterator<RoutineGraphPath<Vertex, Edge>> spliterator = Spliterators.spliteratorUnknownSize(pathToRootIterator, 0);

        return StreamSupport.stream(spliterator, false);
    }

}
