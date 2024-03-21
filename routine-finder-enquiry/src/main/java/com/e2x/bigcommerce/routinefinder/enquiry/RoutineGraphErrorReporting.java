package com.e2x.bigcommerce.routinefinder.enquiry;

import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;

public interface RoutineGraphErrorReporting {
    <T extends Vertex> void errorOn(T vertex, String errorMessage);
    void errorOn(RoutineGraph graph, String errorMessage);
    boolean hasErrors(RoutineGraph graph);
}
