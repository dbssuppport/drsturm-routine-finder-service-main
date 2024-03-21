package com.e2x.bigcommerce.routinefinder.enquiry;

import com.e2x.bigcommerce.routinefinder.enquiry.graph.Edge;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import org.jgrapht.Graph;

import java.util.Set;

/**
 * Routine Graph operations required to process a routine questionnaire.
 * This interface extends the Graph<V, E> to provide specific vertex and edge routine functions.
 */
public interface RoutineGraph extends Graph<Vertex, Edge> {
    /**
     * add and return vertex to the graph
     * @param vertex vertex to add
     * @return vertex added
     */
    Vertex add(Vertex vertex);

    /**
     * creates a default edge from the source vertex to the target vertex
     * @param source source vertex
     * @param target target vertex
     */
    void link(Vertex source, Vertex target);

    /**
     * creates a conditional vertex between source and target vertices
     * @param source source vertex
     * @param onCondition condition on the in-between vertex
     * @param target target vertex
     */
    void link(Vertex source, String onCondition, Vertex target);

    /**
     * find a vertex by id
     * @param id id to find
     * @return found vertex
     */
    Vertex findById(long id);

    /**
     * return all vertices without incoming edges (roots)
     * @return set of vertices
     */
    Set<Vertex> findRoots();

    /**
     * find the parent vertices to a vertex
     * @param vertex vertex
     * @return set of parent vertices
     */
    Set<Vertex> findParentVerticesFor(Vertex vertex);

    /**
     * find child vertices to a vertex
     * @param vertex vertex
     * @return set of child vertices
     */
    Set<Vertex> findChildVerticesFor(Vertex vertex);
}
