package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;

class OptionsNodeValidator extends ErrorReportingValidator<Vertex> implements VertexValidator {

    public OptionsNodeValidator(RoutineGraphErrorReporting routineGraphErrorReporting) {
        super(routineGraphErrorReporting);
    }

    @Override
    public void validateVertex(Vertex vertex, RoutineGraph routineGraph) {
        // nothing to validate on options vertex
    }

    @Override
    public void validateLinkVertex(Vertex vertex, long parentVertexId, RoutineGraph graph) {
        var parentVertex = graph.findById(parentVertexId);

        if (parentVertex.getVertexType() != VertexType.QUESTION) {
            errorOn(vertex, "Options vertex can only be attached to a question.");
        } else {
            if (alreadyHasOptionsNode(graph, parentVertex)) {
                errorOn(vertex, String.format("an options node is already attached to question %s", parentVertexId));
            }
        }
    }

    private boolean alreadyHasOptionsNode(RoutineGraph graph, Vertex parentVertex) {
        return graph
                .findChildVerticesFor(parentVertex)
                .stream()
                .anyMatch(v -> v.getVertexType().equals(VertexType.OPTIONS));
    }
}
