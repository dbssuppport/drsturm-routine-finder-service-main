package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.GraphDelegator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Simple way of delegating graph operations in a super class requiring a runtime context.
 * This allows for this graph instance to be shared across multiple other instances in a thread-safe manner
 * @param <V>
 * @param <E>
 */
public class DelegatedGraph<V, E> extends GraphDelegator<V, E> {

    public DelegatedGraph(Graph<V, E> graph) {
        super(graph);
    }

    public DelegatedGraph(Graph<V, E> graph, Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
        super(graph, vertexSupplier, edgeSupplier);
    }

    public V add(V vertex) {
        super.addVertex(vertex);

        return vertex;
    }

    public void link(V source, V target) {
        if (!containsVertex(target)) {
            add(target);
        }

        super.addEdge(source, target);
    }

    public Set<V> findRoots() {
        return vertexSet()
                .stream()
                .filter(this::isRoot)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<V> findParentVerticesFor(V vertex) {
        return incomingEdgesOf(vertex)
                .stream()
                .map(this::getEdgeSource)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<V> findChildVerticesFor(V vertex) {
        return outgoingEdgesOf(vertex)
                .stream()
                .map(this::getEdgeTarget)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean isRoot(V vertex) {
        return incomingEdgesOf(vertex).size() == 0;
    }
}
