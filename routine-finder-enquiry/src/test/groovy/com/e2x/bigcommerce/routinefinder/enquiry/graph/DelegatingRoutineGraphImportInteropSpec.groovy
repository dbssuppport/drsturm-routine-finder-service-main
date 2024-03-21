package com.e2x.bigcommerce.routinefinder.enquiry.graph

import spock.lang.Specification

class DelegatingRoutineGraphImportInteropSpec extends Specification {

    void 'it adds root vertex to routine graph'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()

        and:
        def vertex = new Vertex()

        and:
        def testObj = new DelegatingRoutineGraphImportInterop(routineGraph)

        when:
        testObj.create(vertex)

        then:
        routineGraph.containsVertex(vertex)
    }

    void 'it adds vertex to routine graph'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()

        and:
        def vertex = new Vertex()

        and:
        def testObj = new DelegatingRoutineGraphImportInterop(routineGraph)

        when:
        testObj.addVertex(routineGraph, vertex)

        then:
        routineGraph.containsVertex(vertex)
    }

    void 'it links vertices to routine graph'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()

        and:
        def childVertex = new Vertex()
        def parentVertex = new Vertex()

        and:
        def testObj = new DelegatingRoutineGraphImportInterop(routineGraph)
        testObj.addVertex(routineGraph, parentVertex)

        when:
        testObj.link(routineGraph, childVertex, parentVertex.getId())

        then:
        routineGraph.containsVertex(childVertex)

        and:
        routineGraph.edgesOf(parentVertex).collect({routineGraph.getEdgeTarget(it) }) == [childVertex]
    }

}
