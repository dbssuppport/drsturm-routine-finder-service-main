package com.e2x.bigcommerce.routinefinder.enquiry.graph

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator
import spock.lang.Specification

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionsVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newRoutineFor

class ValidatingRoutineGraphImportInteropSpec extends Specification {

    ValidatingRoutineGraphImportInterop testObj

    VertexValidator vertexValidator
    RoutineGraphErrorReporting errorReporting

    void setup() {
        vertexValidator = Mock()
        errorReporting = Mock()

        testObj = new ValidatingRoutineGraphImportInterop(errorReporting, vertexValidator)
    }

    void 'it should validate a vertex before returning a graph with vertex'() {
        given:
        def vertex = new Vertex()

        when:
        def routineGraph = testObj.create(vertex)

        then:
        1 * vertexValidator.validateVertex(vertex, _)

        and:
        routineGraph
        routineGraph.containsVertex(vertex)
    }

    void 'it should validate a new vertex added to a graph'() {
        given:
        def vertex = new Vertex()
        def graph = new InMemoryRoutineGraph()

        when:
        testObj.addVertex(graph, vertex)

        then:
        1 * vertexValidator.validateVertex(vertex, graph)

        and:
        graph.containsVertex(vertex)
    }

    void 'it should validate vertex linked to a parent vertex'() {
        given:
        def vertex = new Vertex()
        def parentVertex = new Vertex()

        def graph = new InMemoryRoutineGraph()
        graph.addVertex(vertex)
        graph.addVertex(parentVertex)

        when:
        testObj.link(graph, vertex, parentVertex.getId())

        then:
        1 * vertexValidator.validateLinkVertex(vertex, parentVertex.getId(), graph)

        and:
        graph.outgoingEdgesOf(parentVertex).collect({graph.getEdgeTarget(it)}) == [vertex]
    }

    void 'it should report an error if more than one root node found'() {
        given:
        def graph = new InMemoryRoutineGraph()
        graph.addVertex(new Vertex())
        graph.addVertex(new Vertex())

        when:
        testObj.finalise(graph)

        then:
        1 * errorReporting.errorOn(graph, _)
    }

    void 'it should not report error if leaf nodes are of allowed types'() {
        given:
        def parentVertex = new Vertex()

        def graph = new InMemoryRoutineGraph()
        graph.addVertex(vertex)
        graph.addVertex(parentVertex)

        and:
        graph.link(parentVertex, vertex)

        when:
        testObj.finalise(graph)

        then:
        0 * errorReporting.errorOn(vertex, _)

        where:
        vertex << [newOptionValueVertex('code'), newRoutineFor('steps')]
    }

    void 'it should report error if leaf nodes are not of allowed types'() {
        given:
        def parentVertex = new Vertex()

        def graph = new InMemoryRoutineGraph()
        graph.addVertex(vertex)
        graph.addVertex(parentVertex)

        and:
        graph.link(parentVertex, vertex)

        when:
        testObj.finalise(graph)

        then:
        1 * errorReporting.errorOn(vertex, _)

        where:
        vertex << [newQuestionVertexFor('q-id'), newConditionFor('age is 18'), newOptionsVertex()]
    }
}
