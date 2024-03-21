package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.GraphDelegator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor;

/**
 * In memory graph implementation holding routing graph configuration
 *
 * defaults constructors initialises a DAG graph
 */
public class InMemoryRoutineGraph extends GraphDelegator<Vertex, Edge> implements RoutineGraph {

    public InMemoryRoutineGraph() {
        this(new DirectedAcyclicGraph<>(Edge.class));
    }

    public InMemoryRoutineGraph(Graph<Vertex, Edge> delegate) {
        super(delegate);
    }

    @Override
    public Vertex add(Vertex vertex) {
        super.addVertex(vertex);

        return vertex;
    }

    @Override
    public void link(Vertex source, Vertex target) {
        if (!containsVertex(target)) {
            add(target);
        }

        super.addEdge(source, target);
    }

    @Override
    public void link(Vertex source, String onCondition, Vertex target) {
        var conditionNode = add(newConditionFor(onCondition));

        super.addEdge(source, conditionNode);
        super.addEdge(conditionNode, target);
    }

    @Override
    public Vertex findById(long id) {
        return vertexSet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Vertex> findRoots() {
        return vertexSet()
                .stream()
                .filter(this::isRoot)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Vertex> findParentVerticesFor(Vertex vertex) {
        return incomingEdgesOf(vertex)
                .stream()
                .map(this::getEdgeSource)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Vertex> findChildVerticesFor(Vertex vertex) {
        return outgoingEdgesOf(vertex)
                .stream()
                .map(this::getEdgeTarget)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean isRoot(Vertex vertex) {
        return incomingEdgesOf(vertex).size() == 0;
    }}
