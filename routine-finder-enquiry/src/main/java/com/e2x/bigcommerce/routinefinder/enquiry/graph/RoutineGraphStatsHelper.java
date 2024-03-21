package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoutineGraphStatsHelper {

    public static long routineCountIn(RoutineGraph routineGraph) {
        return routinesIn(routineGraph).count();
    }

    public static Integer longestPathIn(RoutineGraph routineGraph) {
        return allPathLengthsIn(routineGraph)
                .stream()
                .mapToInt(v -> v)
                .max()
                .orElse(0);
    }

    public static List<Integer> allPathLengthsIn(RoutineGraph routineGraph) {
        List<Integer> allPathLengths = Lists.newArrayList();

        routinesIn(routineGraph)
                .forEach(r -> pathLengthToRootFrom(r, 1, routineGraph, allPathLengths));

        return allPathLengths;
    }

    public static Integer pathLengthToRootFrom(Vertex vertex, Integer questionCount, RoutineGraph routineGraph, final List<Integer> collectedPathLengths) {
        var pathLength = routineGraph
                .findParentVerticesFor(vertex)
                .stream()
                .map(v -> {
                    if (v.getVertexType().equals(VertexType.QUESTION)) {
                        return pathLengthToRootFrom(v, questionCount + 1, routineGraph, collectedPathLengths);
                    } else {
                        return pathLengthToRootFrom(v, questionCount, routineGraph, collectedPathLengths);
                    }
                })
                .collect(Collectors.toList());

        if (pathLength.size() == 0) {
            collectedPathLengths.add(questionCount);
        }

        return 0;
    }

    public static Stream<Vertex> routinesIn(RoutineGraph routineGraph) {
        return routineGraph
                .vertexSet()
                .stream()
                .filter(v -> v.getVertexType().equals(VertexType.ROUTINE));
    }
}
