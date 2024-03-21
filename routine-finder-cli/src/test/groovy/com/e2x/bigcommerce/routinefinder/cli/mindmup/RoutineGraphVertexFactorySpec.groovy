package com.e2x.bigcommerce.routinefinder.cli.mindmup


import com.e2x.bigcommerce.routinefinder.enquiry.graph.*
import spock.lang.Specification
import spock.lang.Unroll

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.codeFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.expressionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.stepsFor

@Unroll
class RoutineGraphVertexFactorySpec extends Specification {

    RoutineGraphVertexFactory testObj

    void setup() {
        testObj = new RoutineGraphVertexFactory()
    }

    void 'it should create a question node'() {
        given:
        def node = new MindMupJsonNode(IdGenerator.nextLong())
        node.attributes['title'] = 'question'

        when:
        def vertex = testObj.createVertexFor(node)

        then:
        vertex.vertexType == VertexType.QUESTION
        vertex.getId()
        node.getId()
    }

    void 'it should create a condition node'() {
        given:
        def node = new MindMupJsonNode(IdGenerator.nextLong())
        node.attributes['title'] = "question ${condition} value".toString()

        when:
        def vertex = testObj.createVertexFor(node)

        then:
        vertex.vertexType == VertexType.CONDITION
        vertex.id
        node.id

        and:
        expressionFor(vertex) == "question ${condition} value".toString()

        where:
        condition << ['is', 'is not', 'in', 'not in']
    }

    void 'it should create an options node'() {
        given:
        def node = new MindMupJsonNode(IdGenerator.nextLong())
        node.attributes['title'] = options

        when:
        def vertex = testObj.createVertexFor(node)

        then:
        vertex.vertexType == VertexType.OPTIONS
        vertex.id
        node.id

        where:
        options << ['options', 'OPTIONS', 'Options']
    }

    void 'it should create an option value node'() {
        given:
        def node = new MindMupJsonNode(IdGenerator.nextLong(), new MindMupJsonNode(IdGenerator.nextLong()))
        node.attributes['title'] = 'option value'

        and:
        node.parentNode.attributes['title'] = options

        when:
        def vertex = testObj.createVertexFor(node)

        then:
        vertex.vertexType == VertexType.OPTION_VALUE
        vertex.id
        node.id

        and:
        codeFor(vertex) == 'option value'

        where:
        options << ['options', 'OPTIONS', 'Options']
    }

    void 'it should return a routine node'() {
        given:
        def node = new MindMupJsonNode(IdGenerator.nextLong())
        node.attributes['title'] = "${step} 1: a step".toString()

        when:
        def vertex = testObj.createVertexFor(node)

        then:
        vertex.vertexType == VertexType.ROUTINE
        vertex.id
        node.id

        and:
        stepsFor(vertex) == "step 1: a-step"

        where:
        step << ['step', 'Step', 'STEP']
    }
}
