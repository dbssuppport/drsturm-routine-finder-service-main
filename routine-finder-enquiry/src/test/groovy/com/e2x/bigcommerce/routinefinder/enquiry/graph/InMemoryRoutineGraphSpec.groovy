package com.e2x.bigcommerce.routinefinder.enquiry.graph

import org.jgrapht.Graph
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class InMemoryRoutineGraphSpec extends Specification {

    InMemoryRoutineGraph testObj
    Graph<Vertex, Edge> delegatedGraphMock

    void setup() {
        delegatedGraphMock = Mock(Graph)

        testObj = new InMemoryRoutineGraph(delegatedGraphMock)
    }

    void 'returns empty set if no root found on graph'() {
        given:
        delegatedGraphMock.vertexSet() >> [nonRootVertex()]

        expect:
        testObj.findRoots() == [] as Set
    }

    void 'can find all roots of this graph'() {
        given:
        def expectedVertex1 = rootVertex()
        def expectedVertex2 = rootVertex()

        and:
        delegatedGraphMock.vertexSet() >> [expectedVertex1, nonRootVertex(), expectedVertex2]

        expect:
        testObj.findRoots() == [expectedVertex1, expectedVertex2] as Set
    }

    void 'can find a vertex by id'() {
        given:
        def vertex = rootVertex()
        vertex.id >> id

        and:
        delegatedGraphMock.vertexSet() >> [vertex]

        expect:
        if (found) {
            assert testObj.findById(1) == vertex
        } else {
            assert testObj.findById(1) == null
        }

        where:
        id  | found
        1   | true
        2   | false
    }

    private Vertex nonRootVertex(boolean triggered = true) {
        vertex(false, triggered)
    }

    private Vertex rootVertex(boolean triggered = true) {
        vertex(true, triggered)
    }

    private Vertex vertex(boolean root, boolean triggered = true) {
        def vertex = Mock(Vertex)
        delegatedGraphMock.incomingEdgesOf(vertex) >> { root ? [] : [Mock(Edge)] }

        vertex.isTriggered(_) >> triggered

        vertex
    }

    private Edge edge() {
        Mock(Edge)
    }
}
