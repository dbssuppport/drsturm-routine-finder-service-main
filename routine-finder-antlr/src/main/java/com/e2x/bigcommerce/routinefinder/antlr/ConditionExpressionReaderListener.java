package com.e2x.bigcommerce.routinefinder.antlr;

import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderBaseListener;
import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderParser;

public class ConditionExpressionReaderListener extends RoutineFinderBaseListener {

    private final ConditionExpressionReader conditionExpressionReader = new ConditionExpressionReader();
    private ConditionExpression conditionExpression;

    public ConditionExpression getEvaluatedConditionExpression() {
        return conditionExpression;
    }

    @Override
    public void enterMatch_rule(RoutineFinderParser.Match_ruleContext ctx) {
        conditionExpression = conditionExpressionReader.read(ctx);
    }

}
