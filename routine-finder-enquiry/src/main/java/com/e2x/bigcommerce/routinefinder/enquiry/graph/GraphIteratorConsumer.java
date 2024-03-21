package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;

import java.util.function.Consumer;

public class GraphIteratorConsumer {

    private final GraphIteratorFactory graphIteratorFactory;

    public GraphIteratorConsumer(GraphIteratorFactory graphIteratorFactory) {
        this.graphIteratorFactory = graphIteratorFactory;
    }

    public void iterateOn(RoutineGraph routineGraph, Consumer<Vertex> vertexConsumer) {
        var iterator = graphIteratorFactory.createIterator(routineGraph);

        while (iterator.hasNext()) {
            var vertex = iterator.next();

            vertexConsumer.accept(vertex);
        }
    }
}
