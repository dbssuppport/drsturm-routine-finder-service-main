package com.e2x.bigcommerce.routinefinder.antlr;

import lombok.Getter;

import java.util.List;

@Getter
public class ConditionExpressionException extends RuntimeException {
    private final List<SyntaxError> errors;

    public ConditionExpressionException(List<SyntaxError> errors) {

        this.errors = errors;
    }

}
