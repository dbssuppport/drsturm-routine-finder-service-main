package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.antlr.ConditionExpressionSyntaxValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.expressionFor;

@Slf4j
class ConditionExpressionNodeValidator extends ErrorReportingValidator<Vertex> implements VertexValidator {
    private static final List<VertexType> ALLOWED_PARENT_TYPES = Lists.newArrayList(VertexType.CONDITION, VertexType.QUESTION);
    private final ConditionExpressionSyntaxValidator conditionExpressionSyntaxValidator;
    private final ConditionExpressionValidator conditionExpressionValidator;

    public ConditionExpressionNodeValidator(RoutineGraphErrorReporting routineGraphErrorReporting, ConditionExpressionValidator conditionExpressionValidator) {
        super(routineGraphErrorReporting);
        this.conditionExpressionValidator = conditionExpressionValidator;

        this.conditionExpressionSyntaxValidator = new ConditionExpressionSyntaxValidator();
    }

    @Override
    public void validateVertex(Vertex vertex, RoutineGraph routineGraph) {
        var conditionExpression = expressionFor(vertex);

        conditionExpressionSyntaxValidator.validate(conditionExpression, syntaxError -> errorOn(vertex, String.format("expression is invalid: %s", syntaxError.getMsg())));

        if (conditionExpressionValidator != null) {
            conditionExpressionValidator.validate(conditionExpression, errorMessage -> errorOn(vertex, errorMessage));
        }
    }

    @Override
    public void validateLinkVertex(Vertex vertex, long parentVertexId, RoutineGraph graph) {
        var parentVertex = graph.findById(parentVertexId);

        if (parentIsNotAllowed(parentVertex)) {
            errorOn(vertex, String.format("Parent vertex of type %s is not allowed for condition node %s", parentVertex.getVertexType(), expressionFor(vertex)));
        }

        if (sameConditionAlreadyLinked(vertex, parentVertex, graph)) {
            errorOn(vertex, String.format("A condition with %s has already been found on parent node %s", expressionFor(vertex), parentVertex.getId()));
        }
    }

    private boolean sameConditionAlreadyLinked(Vertex vertex, Vertex parentVertex, RoutineGraph graph) {
        return graph.findChildVerticesFor(parentVertex)
                .stream()
                .anyMatch(v -> v.getVertexType().equals(VertexType.CONDITION) && expressionFor(v).equals(expressionFor(vertex)));
    }

    private boolean parentIsNotAllowed(Vertex parentVertex) {
        return !ALLOWED_PARENT_TYPES.contains(parentVertex.getVertexType());
    }
}
