package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;

/**
 * Class which will establish the current progress of a routine enquiry.
 *
 * As a user answers questions for a routine enquiry, this calculator will determine for each question nodes their respective progress in the path to a routine.
 *
 * example:
 *
 * question 1 (25%) => question 2 (50%) => question 3 (75%) => routine (100%)
 */
public class RoutineProgressCalculator {

    public RoutineGraph process(RoutineGraph routineGraph) {
        routineGraph
                .vertexSet()
                .stream()
                .filter((v) -> v.getVertexType().equals(VertexType.ROUTINE))
                .forEach((v) -> {
                    calculateVertexProgress(routineGraph, v, 1);
                });

        return routineGraph;
    }

    private void calculateVertexProgress(RoutineGraph graph, Vertex source, Integer length) {
        graph
                .findParentVerticesFor(source)
                .forEach(parentVertex -> {
                    Integer nextLength = length;
                    if (source.getVertexType().equals(VertexType.QUESTION)) {
                        nextLength++;
                    }

                    calculateVertexProgress(graph, parentVertex, nextLength);
                });

        Integer currentProgress = VertexUtils.progressFor(source);
        if (currentProgress == null || currentProgress < length) {
            VertexUtils.setProgressFor(source, length);
        }
    }
}
