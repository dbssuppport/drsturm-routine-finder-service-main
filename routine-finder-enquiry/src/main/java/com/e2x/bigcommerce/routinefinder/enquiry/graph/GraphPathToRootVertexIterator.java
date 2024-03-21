package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.google.common.collect.Queues;
import org.jgrapht.Graph;

import java.util.Iterator;
import java.util.Queue;

/**
 * iterates through all parent nodes from starting vertex to root node.
 * Only usable on a Directed Acyclic Graph (where at least one root node exists)
 *
 * note: this does not track visited vertices (could be improved)
 * @param <V>
 * @param <E>
 */
public class GraphPathToRootVertexIterator<V, E> implements Iterator<V> {
    private final Graph<V, E> graph;
    private final Queue<V> visitingVertices = Queues.newArrayDeque();

    public GraphPathToRootVertexIterator(V startingVertex, Graph<V, E> graph) {
        visitingVertices.add(startingVertex);
        this.graph = graph;
    }

    @Override
    public boolean hasNext() {
        return visitingVertices.size() > 0;
    }

    @Override
    public V next() {
        var vertex = visitingVertices.poll();

        if (vertex != null) {
            enqueueParentsOf(vertex);
        }

        return vertex;
    }

    private void enqueueParentsOf(V vertex) {
        graph
                .incomingEdgesOf(vertex)
                .forEach(e -> visitingVertices.add(graph.getEdgeSource(e)));
    }
}

