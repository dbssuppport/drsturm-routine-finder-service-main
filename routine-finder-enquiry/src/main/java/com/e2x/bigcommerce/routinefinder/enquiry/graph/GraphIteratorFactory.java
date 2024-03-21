package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import org.jgrapht.traverse.GraphIterator;

public interface GraphIteratorFactory {
    GraphIterator<Vertex, Edge> createIterator(RoutineGraph routineGraph);
}
