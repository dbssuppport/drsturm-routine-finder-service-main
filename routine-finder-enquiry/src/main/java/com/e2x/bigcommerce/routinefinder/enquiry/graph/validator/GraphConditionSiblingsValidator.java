package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.DepthFirstGraphIteratorFactory;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.GraphIteratorConsumer;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;

import java.util.HashSet;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.expressionFor;
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.getRoutinePathToRootFor;

public class GraphConditionSiblingsValidator extends ErrorReportingValidator<Vertex> {

    public GraphConditionSiblingsValidator(RoutineGraphErrorReporting routineGraphErrorReporting) {
        super(routineGraphErrorReporting);
    }

    public void validate(RoutineGraph graph) {
        var iterator = new GraphIteratorConsumer(new DepthFirstGraphIteratorFactory());
        var validator = new SiblingConditionsValidator();
        var visitedParentVertices = new HashSet<Vertex>();

        iterator.iterateOn(graph, vertex -> {
            if (VertexType.CONDITION.equals(vertex.getVertexType())) {
                graph.findParentVerticesFor(vertex).forEach(parentVertex -> {
                    if (validator.conflictingExpressionPresent(vertex, parentVertex, graph)) {
                        if (!visitedParentVertices.contains(parentVertex)) {
                            errorOn(vertex, String.format("Sibling condition has been found which evaluates to true on one of the value contained in condition %s. Check child conditions at %s", expressionFor(vertex), getRoutinePathToRootFor(parentVertex, graph)));
                            visitedParentVertices.add(parentVertex);
                        }
                    }
                });
            }
        });
    }

}
