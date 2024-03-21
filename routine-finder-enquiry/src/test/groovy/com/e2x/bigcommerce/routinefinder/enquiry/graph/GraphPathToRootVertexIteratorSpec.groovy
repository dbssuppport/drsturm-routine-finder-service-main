package com.e2x.bigcommerce.routinefinder.enquiry.graph

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class GraphPathToRootVertexIteratorSpec extends Specification {

    void 'can iterate over vertices found on path to root from starting vertex'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        def rootVertex = graph.add(new Vertex(-1L))
        def vertex1 = graph.add(new Vertex(1))
        def vertex2 = graph.add(new Vertex(2))
        def vertex3 = graph.add(new Vertex(3))
        def vertex4 = graph.add(new Vertex(4))
        def vertex5 = graph.add(new Vertex(5))
        def vertex6 = graph.add(new Vertex(6))
        def vertex7 = graph.add(new Vertex(7))

        and:
        graph.link(rootVertex, vertex1)
        graph.link(rootVertex, vertex2)
        graph.link(vertex1, vertex3)
        graph.link(vertex1, vertex4)
        graph.link(vertex2, vertex5)
        graph.link(vertex3, vertex6)
        graph.link(vertex4, vertex7)
        graph.link(vertex7, vertex6)

        and:
        def testObj = new GraphPathToRootVertexIterator(vertex5, graph)

        when:
        def collectedVertices = testObj.collect({ it }).toSet()

        then:
        collectedVertices == [rootVertex, vertex2, vertex5] as Set

        when:
        testObj = new GraphPathToRootVertexIterator(vertex6, graph)
        collectedVertices = testObj.collect({ it }).toSet()

        then:
        collectedVertices == [rootVertex, vertex1, vertex3, vertex4, vertex6, vertex7] as Set

        when:
        testObj = new GraphPathToRootVertexIterator(vertex7, graph)
        collectedVertices = testObj.collect({ it }).toSet()

        then:
        collectedVertices == [rootVertex, vertex4, vertex1, vertex7] as Set
    }
}
