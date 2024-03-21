package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class DepthFirstGraphIteratorFactory implements GraphIteratorFactory {

    @Override
    public GraphIterator<Vertex, Edge> createIterator(RoutineGraph routineGraph) {
        var rootNode = routineGraph
                .findRoots()
                .stream()
                .findFirst()
                .orElseThrow();

        return new DepthFirstIterator<>(routineGraph, rootNode);
    }

}
