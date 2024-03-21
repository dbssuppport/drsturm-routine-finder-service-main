package com.e2x.bigcommerce.routinefinder.enquiry.graph.data

import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor

class TestConditionBuilder {
    static final Closure<Vertex> CONDITION_NODE = { String expression -> newConditionFor(expression) }
}
