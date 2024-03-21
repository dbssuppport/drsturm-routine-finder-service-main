package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import org.jgrapht.GraphType;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public class DelegatedRoutineGraph implements RoutineGraph {
    private final RoutineGraph delegate;

    public DelegatedRoutineGraph(RoutineGraph delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<Edge> getAllEdges(Vertex sourceVertex, Vertex targetVertex) {
        return delegate.getAllEdges(sourceVertex, targetVertex);
    }

    @Override
    public Edge getEdge(Vertex sourceVertex, Vertex targetVertex) {
        return delegate.getEdge(sourceVertex, targetVertex);
    }

    @Override
    public Supplier<Vertex> getVertexSupplier() {
        return delegate.getVertexSupplier();
    }

    @Override
    public Supplier<Edge> getEdgeSupplier() {
        return delegate.getEdgeSupplier();
    }

    @Override
    public Edge addEdge(Vertex sourceVertex, Vertex targetVertex) {
        return delegate.addEdge(sourceVertex, targetVertex);
    }

    @Override
    public boolean addEdge(Vertex sourceVertex, Vertex targetVertex, Edge edge) {
        return delegate.addEdge(sourceVertex, targetVertex, edge);
    }

    @Override
    public Vertex addVertex() {
        return delegate.addVertex();
    }

    @Override
    public boolean addVertex(Vertex vertex) {
        return delegate.addVertex(vertex);
    }

    @Override
    public boolean containsEdge(Vertex sourceVertex, Vertex targetVertex) {
        return delegate.containsEdge(sourceVertex, targetVertex);
    }

    @Override
    public boolean containsEdge(Edge edge) {
        return delegate.containsEdge(edge);
    }

    @Override
    public boolean containsVertex(Vertex vertex) {
        return delegate.containsVertex(vertex);
    }

    @Override
    public Set<Edge> edgeSet() {
        return delegate.edgeSet();
    }

    @Override
    public int degreeOf(Vertex vertex) {
        return delegate.degreeOf(vertex);
    }

    @Override
    public Set<Edge> edgesOf(Vertex vertex) {
        return delegate.edgesOf(vertex);
    }

    @Override
    public int inDegreeOf(Vertex vertex) {
        return delegate.inDegreeOf(vertex);
    }

    @Override
    public Set<Edge> incomingEdgesOf(Vertex vertex) {
        return delegate.incomingEdgesOf(vertex);
    }

    @Override
    public int outDegreeOf(Vertex vertex) {
        return delegate.outDegreeOf(vertex);
    }

    @Override
    public Set<Edge> outgoingEdgesOf(Vertex vertex) {
        return delegate.outgoingEdgesOf(vertex);
    }

    @Override
    public boolean removeAllEdges(Collection<? extends Edge> edges) {
        return delegate.removeAllEdges(edges);
    }

    @Override
    public Set<Edge> removeAllEdges(Vertex sourceVertex, Vertex targetVertex) {
        return delegate.removeAllEdges(sourceVertex, targetVertex);
    }

    @Override
    public boolean removeAllVertices(Collection<? extends Vertex> vertices) {
        return delegate.removeAllVertices(vertices);
    }

    @Override
    public Edge removeEdge(Vertex sourceVertex, Vertex targetVertex) {
        return delegate.removeEdge(sourceVertex, targetVertex);
    }

    @Override
    public boolean removeEdge(Edge edge) {
        return delegate.removeEdge(edge);
    }

    @Override
    public boolean removeVertex(Vertex vertex) {
        return delegate.removeVertex(vertex);
    }

    @Override
    public Set<Vertex> vertexSet() {
        return delegate.vertexSet();
    }

    @Override
    public Vertex getEdgeSource(Edge edge) {
        return delegate.getEdgeSource(edge);
    }

    @Override
    public Vertex getEdgeTarget(Edge edge) {
        return delegate.getEdgeTarget(edge);
    }

    @Override
    public GraphType getType() {
        return delegate.getType();
    }

    @Override
    public double getEdgeWeight(Edge edge) {
        return delegate.getEdgeWeight(edge);
    }

    @Override
    public void setEdgeWeight(Edge edge, double weight) {
        delegate.setEdgeWeight(edge, weight);
    }

    @Override
    public Vertex add(Vertex vertex) {
        return delegate.add(vertex);
    }

    @Override
    public void link(Vertex source, Vertex target) {
        delegate.link(source, target);
    }

    @Override
    public void link(Vertex source, String onCondition, Vertex target) {
        delegate.link(source, onCondition, target);
    }

    @Override
    public Vertex findById(long id) {
        return delegate.findById(id);
    }

    @Override
    public Set<Vertex> findRoots() {
        return delegate.findRoots();
    }

    @Override
    public Set<Vertex> findParentVerticesFor(Vertex vertex) {
        return delegate.findParentVerticesFor(vertex);
    }

    @Override
    public Set<Vertex> findChildVerticesFor(Vertex vertex) {
        return delegate.findChildVerticesFor(vertex);
    }
}
