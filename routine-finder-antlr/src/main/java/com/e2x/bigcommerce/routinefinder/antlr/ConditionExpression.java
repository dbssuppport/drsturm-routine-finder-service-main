package com.e2x.bigcommerce.routinefinder.antlr;

import lombok.Data;

import java.util.List;

@Data
public class ConditionExpression {
    private final String questionId;
    private final List<Object> answersToMatch;
    private final boolean isNegated;
    private final Operand operand;

    public static enum Operand {
        IN,
        IS
    }
}
