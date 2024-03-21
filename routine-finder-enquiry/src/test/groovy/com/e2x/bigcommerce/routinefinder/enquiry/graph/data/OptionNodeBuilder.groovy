package com.e2x.bigcommerce.routinefinder.enquiry.graph.data

import com.e2x.bigcommerce.routinefinder.enquiry.Option
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionsVertex

class OptionNodeBuilder {

    static LIGHT = new Option('light')
    static DARK = new Option('dark')
    static VERY_DARK = new Option('very_dark')

    static Closure<Vertex> OPTION_NODE = { newOptionsVertex() }
    static Closure<Vertex> OPTION_VALUE_NODE = { String code -> newOptionValueVertex(code) }

}
