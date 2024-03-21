package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.antlr.ConditionExpression;
import com.e2x.bigcommerce.routinefinder.antlr.ConditionExpressionReaderListener;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;

import java.util.Set;
import java.util.stream.Collectors;

import static com.e2x.bigcommerce.routinefinder.antlr.ConditionExpressionWalker.walk;
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.expressionFor;

public class SiblingConditionsValidator {

    public boolean conflictingExpressionPresent(Vertex vertex, Vertex parentVertex, RoutineGraph routineGraph) {
        var conditionExpression = readConditionExpressionFor(vertex);

        return routineGraph
                .findChildVerticesFor(parentVertex)
                .stream()
                .filter(v -> VertexType.CONDITION.equals(v.getVertexType()))
                .filter(v -> v != vertex)
                .anyMatch(siblingCondition -> {
                    var siblingExpression = readConditionExpressionFor(siblingCondition);

                    return expressionsConflicts(conditionExpression, siblingExpression);
                });
    }

    private static boolean expressionsConflicts(ConditionExpression one, ConditionExpression other) {
        Set<Object> result = one.getAnswersToMatch().stream()
                .distinct()
                .filter(a -> other.getAnswersToMatch().contains(a))
                .collect(Collectors.toSet());

        if ((one.isNegated() && other.isNegated()) || (!one.isNegated() && !other.isNegated())) {
            return result.size() > 0;
        } else {
            return result.size() == 0;
        }
    }

    private static ConditionExpression readConditionExpressionFor(Vertex vertex) {
        var conditionExpressionReader = new ConditionExpressionReaderListener();
        walk(expressionFor(vertex), conditionExpressionReader);

        return conditionExpressionReader.getEvaluatedConditionExpression();
    }

}
