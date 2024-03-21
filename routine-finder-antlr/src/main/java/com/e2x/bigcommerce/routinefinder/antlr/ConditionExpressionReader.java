package com.e2x.bigcommerce.routinefinder.antlr;

import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderParser;
import com.google.common.collect.Lists;

import java.util.List;

public class ConditionExpressionReader {

    public ConditionExpression read(RoutineFinderParser.Match_ruleContext ctx) {
        var questionId = ctx.QUESTION().getText();
        var valuesToMatch = getValuesToMatch(ctx);
        var isNot = false;
        ConditionExpression.Operand operand = ConditionExpression.Operand.IN;

        if (ctx.in_operand() != null) {
            isNot = ctx.in_operand().NOT() != null;
        } else if (!ctx.is_operand().isEmpty()) {
            operand = ConditionExpression.Operand.IS;
            isNot = ctx.is_operand().NOT() != null;
        }

        return new ConditionExpression(questionId, valuesToMatch, isNot, operand);
    }

    private List<Object> getValuesToMatch(RoutineFinderParser.Match_ruleContext ctx) {
        var values = Lists.newLinkedList();

        if (ctx.VALUE() != null) {
            values.add(ctx.VALUE().getText());
        }

        getValueFor(values, ctx.values());

        return values;
    }

    private void getValueFor(List<Object> values, RoutineFinderParser.ValuesContext ctx) {
        if (ctx != null) {
            if (ctx.values() != null && !ctx.values().isEmpty()) {
                getValueFor(values, ctx.values());
            }

            values.add(ctx.VALUE().getText());
        }
    }
}
