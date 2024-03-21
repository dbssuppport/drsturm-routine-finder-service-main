package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.google.common.collect.Queues;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * iterates through all parent nodes from starting vertex to root node.
 * Only usable on a Directed Acyclic Graph (where at least one root node exists)
 *
 * note: this does not track visited vertices (could be improved)
 * @param <V>
 * @param <E>
 */
public class GraphPathToRootIterator<V, E> implements Iterator<RoutineGraphPath<V, E>> {
    private final Graph<V, E> graph;
    private final Queue<RoutineGraphPath<V, E>> routineGraphPathsToVisit = Queues.newArrayDeque();

    public GraphPathToRootIterator(V startingVertex, Graph<V, E> graph) {
        var graphPath = new DirectedAcyclicGraph<>(graph.getVertexSupplier(), graph.getEdgeSupplier(), false);
        var firstPath = new RoutineGraphPath<>(startingVertex, graphPath);
        routineGraphPathsToVisit.add(firstPath);
        this.graph = graph;
    }

    @Override
    public boolean hasNext() {
        return routineGraphPathsToVisit.size() > 0;
    }

    @Override
    public RoutineGraphPath<V, E> next() {
        var nextPath = routineGraphPathsToVisit.poll();

        if (nextPath == null) {
            return null;
        }

        traverseToRoot(nextPath.getStartVertex(), nextPath);

        return nextPath;
    }

    private void traverseToRoot(V vertex, RoutineGraphPath<V, E> routineGraphPath) {
        final AtomicInteger index = new AtomicInteger(0);

        graph
                .incomingEdgesOf(vertex)
                .forEach(edge -> {
                    var parentVertex = graph.getEdgeSource(edge);
                    routineGraphPath.link(parentVertex, vertex);
                    routineGraphPath.setStartVertex(parentVertex);

                    if (index.getAndIncrement() == 0) {
                        traverseToRoot(parentVertex, routineGraphPath);
                    } else {
                        var pathToVisit = routineGraphPath.copy();
                        routineGraphPathsToVisit.add(pathToVisit);
                    }
                });


    }

}

