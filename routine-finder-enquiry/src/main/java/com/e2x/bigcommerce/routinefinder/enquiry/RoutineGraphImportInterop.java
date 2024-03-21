package com.e2x.bigcommerce.routinefinder.enquiry;

import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;

/**
 * interface describing available interaction to a routine graph instance during import operations
 */
public interface RoutineGraphImportInterop {

    /**
     * returns a routine graph with rootVertex as the first node.
     * @param vertex vertex to be added to routine graph
     * @return routine graph with added vertex
     */
    RoutineGraph create(Vertex vertex);

    /**
     * adds a new vertex to the given routine graph
     * @param graph routine graph receiving vertex
     * @param vertex vertex to add
     */
    void addVertex(RoutineGraph graph, Vertex vertex);

    /**
     * adds an edge between given vertex to the parent vertex id in the given routine graph
     * @param graph graph to link vertices
     * @param vertex vertex target
     * @param parentVertexId parent vertex id
     */
    void link(RoutineGraph graph, Vertex vertex, long parentVertexId);

    /**
     * this method should be invoked when an import has completed.
     * @param graph graph to link vertices
     */
    void finalise(RoutineGraph graph);

}
