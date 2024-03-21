package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import java.util.function.Consumer;

public interface ConditionExpressionValidator {
    void validate(String expression, Consumer<String> errorConsumer);
}
