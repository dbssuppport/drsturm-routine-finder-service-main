package com.e2x.bigcommerce.routinefinder.antlr;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoutineFinderExpressionInterpreter implements ConditionExpressionEvaluator {

    @Override
    public boolean evaluate(String expression, QuestionAnswerFinder questionAnswerFinder) {
        var listener = new ConditionExpressionListener(questionAnswerFinder);

        ConditionExpressionWalker.walk(expression, listener);

        return listener.evaluate();
    }

}
