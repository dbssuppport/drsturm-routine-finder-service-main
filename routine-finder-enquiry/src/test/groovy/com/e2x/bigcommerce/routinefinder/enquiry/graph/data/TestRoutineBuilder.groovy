package com.e2x.bigcommerce.routinefinder.enquiry.graph.data

import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newRoutineFor

class TestRoutineBuilder {
    static final Vertex SIMPLE_ROUTINE = newRoutineFor('step 1: simple cleanser')
    static final Vertex MEDIUM_ROUTINE = newRoutineFor('step 1: medium cleanser')
}
