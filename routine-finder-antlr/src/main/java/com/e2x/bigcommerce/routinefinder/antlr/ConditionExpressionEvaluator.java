package com.e2x.bigcommerce.routinefinder.antlr;

public interface ConditionExpressionEvaluator {
    boolean evaluate(String expression, QuestionAnswerFinder questionAnswerFinder);
}
