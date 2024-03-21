package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.AbstractBaseGraph;

import java.util.ArrayList;
import java.util.List;

public class RoutineGraphPath<V, E> implements GraphPath<V, E> {

    private final Graph<V, E> routineGraph;
    private final V endVertex;
    private V startVertex;

    public RoutineGraphPath(V startVertex, Graph<V, E> graph) {
        this(startVertex, startVertex, graph);
    }

    public RoutineGraphPath(V startVertex, V endVertex, Graph<V, E> graph) {
        this.routineGraph = graph;
        this.startVertex = startVertex;
        this.endVertex = endVertex;

        this.routineGraph.addVertex(startVertex);
        this.routineGraph.addVertex(endVertex);
    }

    @Override
    public Graph<V, E> getGraph() {
        return routineGraph;
    }

    @Override
    public V getStartVertex() {
        return this.startVertex;
    }

    @Override
    public V getEndVertex() {
        return this.endVertex;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public List<V> getVertexList() {
        return new ArrayList<>(routineGraph.vertexSet());
    }

    @Override
    public List<E> getEdgeList() {
        return new ArrayList<>(routineGraph.edgeSet());
    }

    public RoutineGraphPath<V, E> copy() {
        return new RoutineGraphPath<>(startVertex, endVertex, (Graph<V, E>) ((AbstractBaseGraph<V, E>) routineGraph).clone());
    }

    public void link(V sourceVertex, V targetVertex) {
        this.routineGraph.addVertex(sourceVertex);
        this.routineGraph.addVertex(targetVertex);
        this.routineGraph.addEdge(sourceVertex, targetVertex);
    }

    public void setStartVertex(V startVertex) {
        this.startVertex = startVertex;
        this.routineGraph.addVertex(this.startVertex);
    }
}
