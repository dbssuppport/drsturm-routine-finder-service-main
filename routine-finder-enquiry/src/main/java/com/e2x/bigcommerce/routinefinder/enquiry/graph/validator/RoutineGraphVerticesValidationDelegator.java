package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;
import lombok.Getter;

import java.util.Map;

public class RoutineGraphVerticesValidationDelegator implements VertexValidator {

    @Getter
    private final Map<VertexType, VertexValidator> verticesValidatorMap;

    public RoutineGraphVerticesValidationDelegator(Map<VertexType, VertexValidator> verticesValidatorMap) {
        this.verticesValidatorMap = verticesValidatorMap;
    }

    @Override
    public void validateVertex(Vertex vertex, RoutineGraph routineGraph) {
        verticesValidatorMap.get(vertex.getVertexType()).validateVertex(vertex, routineGraph);
    }

    @Override
    public void validateLinkVertex(Vertex vertex, long parentVertexId, RoutineGraph routineGraph) {
        verticesValidatorMap.get(vertex.getVertexType()).validateLinkVertex(vertex, parentVertexId, routineGraph);
    }
}
