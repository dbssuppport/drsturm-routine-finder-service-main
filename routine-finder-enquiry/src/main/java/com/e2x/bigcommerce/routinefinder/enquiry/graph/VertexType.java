package com.e2x.bigcommerce.routinefinder.enquiry.graph;

public enum VertexType {
    QUESTION(1),
    CONDITION(2),
    ROUTINE(0),
    OPTIONS(0),
    OPTION_VALUE(0),
    OTHER(1);

    private int priority;

    VertexType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
