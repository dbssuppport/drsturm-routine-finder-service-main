package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphImportInterop;

/**
 * Simple class delegating operations to a graph given during its instantiation
 */
public class DelegatingRoutineGraphImportInterop implements RoutineGraphImportInterop {

    private final RoutineGraph routineGraph;

    public DelegatingRoutineGraphImportInterop(RoutineGraph routineGraph) {
        this.routineGraph = routineGraph;
    }

    @Override
    public RoutineGraph create(Vertex vertex) {
        routineGraph.addVertex(vertex);
        return routineGraph;
    }

    @Override
    public void addVertex(RoutineGraph graph, Vertex vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void link(RoutineGraph graph, Vertex vertex, long parentVertexId) {
        var parentVertex = graph.findById(parentVertexId);
        graph.link(parentVertex, vertex);
    }

    @Override
    public void finalise(RoutineGraph graph) {
        // nothing to be completed here.
    }

}
