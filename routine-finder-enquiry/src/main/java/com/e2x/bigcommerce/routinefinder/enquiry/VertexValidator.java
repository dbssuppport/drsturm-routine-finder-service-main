package com.e2x.bigcommerce.routinefinder.enquiry;

import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;

public interface VertexValidator {
    void validateVertex(Vertex vertex, RoutineGraph routineGraph);
    void validateLinkVertex(Vertex vertex, long parentVertexId, RoutineGraph graph);
}
