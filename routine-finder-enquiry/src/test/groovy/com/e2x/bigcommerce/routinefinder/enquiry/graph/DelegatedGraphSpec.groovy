package com.e2x.bigcommerce.routinefinder.enquiry.graph

import org.jgrapht.Graph
import spock.lang.Specification

class DelegatedGraphSpec extends Specification {
    Graph graphMock
    DelegatedGraph testObj

    void setup() {
        graphMock = Mock()
        testObj = new DelegatedGraph(graphMock)
    }

    void 'verify delegation to Set getAllEdges(V sourceVertex, V targetVertex)'() {
        when:
        testObj.getAllEdges(null, null)

        then:
        1 * graphMock.getAllEdges(null, null)
    }

    void 'verify delegation to E getEdge(V sourceVertex, V targetVertex)'() {
        when:
        testObj.getEdge(null, null)

        then:
        1 * graphMock.getEdge(null, null)
    }

    void 'verify delegation to Supplier getVertexSupplier()'() {
        when:
        testObj.getVertexSupplier()

        then:
        1 * graphMock.getVertexSupplier()
    }

    void 'verify delegation to Supplier getEdgeSupplier()'() {
        when:
        testObj.getEdgeSupplier()

        then:
        1 * graphMock.getEdgeSupplier()
    }

    void 'verify delegation to E addEdge(V sourceVertex, V targetVertex)'() {
        when:
        testObj.addEdge(null, null)

        then:
        1 * graphMock.addEdge(null, null)
    }

    void 'verify delegation to boolean addEdge(V sourceVertex, V targetVertex, E e)'() {
        when:
        testObj.addEdge(null, null, null)

        then:
        1 * graphMock.addEdge(null, null, null)
    }

    void 'verify delegation to V addVertex()'() {
        when:
        testObj.addVertex()

        then:
        1 * graphMock.addVertex()
    }

    void 'verify delegation to boolean addVertex(V v)'() {
        when:
        testObj.addVertex(null)

        then:
        1 * graphMock.addVertex(null)
    }

    void 'verify delegation to boolean containsEdge(V sourceVertex, V targetVertex)'() {
        when:
        testObj.containsEdge(null, null)

        then:
        1 * graphMock.getEdge(null, null)
    }

    void 'verify delegation to boolean containsEdge(E e)'() {
        when:
        testObj.containsEdge(null)

        then:
        1 * graphMock.containsEdge(null)
    }

    void 'verify delegation to boolean containsVertex(V v)'() {
        when:
        testObj.containsVertex(null)

        then:
        1 * graphMock.containsVertex(null)
    }

    void 'verify delegation to Set edgeSet()'() {
        when:
        testObj.edgeSet()

        then:
        1 * graphMock.edgeSet()
    }

    void 'verify delegation to int degreeOf(V vertex)'() {
        when:
        testObj.degreeOf(null)

        then:
        1 * graphMock.degreeOf(null)
    }

    void 'verify delegation to Set edgesOf(V vertex)'() {
        when:
        testObj.edgesOf(null)

        then:
        1 * graphMock.edgesOf(null)
    }

    void 'verify delegation to int inDegreeOf(V vertex)'() {
        when:
        testObj.inDegreeOf(null)

        then:
        1 * graphMock.inDegreeOf(null)
    }

    void 'verify delegation to Set incomingEdgesOf(V vertex)'() {
        when:
        testObj.incomingEdgesOf(null)

        then:
        1 * graphMock.incomingEdgesOf(null)
    }

    void 'verify delegation to int outDegreeOf(V vertex)'() {
        when:
        testObj.outDegreeOf(null)

        then:
        1 * graphMock.outDegreeOf(null)
    }

    void 'verify delegation to Set outgoingEdgesOf(V vertex)'() {
        when:
        testObj.outgoingEdgesOf(null)

        then:
        1 * graphMock.outgoingEdgesOf(null)
    }

    void 'verify delegation to boolean removeAllEdges(Collection edges)'() {
        given:
        def edge = Mock(Edge)

        when:
        testObj.removeAllEdges([edge])

        then:
        1 * graphMock.removeEdge(edge)
    }

    void 'verify delegation to Set removeAllEdges(V sourceVertex, V targetVertex)'() {
        given:
        def edge = Mock(Edge)
        graphMock.getAllEdges(null, null) >> [edge]

        when:
        testObj.removeAllEdges(null, null)

        then:
        1 * graphMock.removeEdge(edge)
    }

    void 'verify delegation to boolean removeAllVertices(Collection vertices)'() {
        given:
        def edge = Mock(Edge)

        when:
        testObj.removeAllEdges([edge])

        then:
        1 * graphMock.removeEdge(edge)
    }

    void 'verify delegation to E removeEdge(V sourceVertex, V targetVertex)'() {
        given:
        def edge = Mock(Edge)
        graphMock.getAllEdges(null, null) >> [edge]

        when:
        testObj.removeAllEdges(null, null)

        then:
        1 * graphMock.removeEdge(edge)
    }

    void 'verify delegation to boolean removeEdge(E e)'() {
        given:
        def edge = Mock(Edge)

        when:
        testObj.removeAllEdges([edge])

        then:
        1 * graphMock.removeEdge(edge)
    }

    void 'verify delegation to boolean removeVertex(V v)'() {
        when:
        testObj.removeVertex(null)

        then:
        1 * graphMock.removeVertex(null)
    }

    void 'verify delegation to Set vertexSet()'() {
        when:
        testObj.vertexSet()

        then:
        1 * graphMock.vertexSet()
    }

    void 'verify delegation to V getEdgeSource(E e)'() {
        when:
        testObj.getEdgeSource(null)

        then:
        1 * graphMock.getEdgeSource(null)
    }

    void 'verify delegation to V getEdgeTarget(E e)'() {
        when:
        testObj.getEdgeTarget(null)

        then:
        1 * graphMock.getEdgeTarget(null)
    }

    void 'verify delegation to GraphType getType()'() {
        when:
        testObj.getType()

        then:
        1 * graphMock.getType()
    }

    void 'verify delegation to double getEdgeWeight(E e)'() {
        when:
        testObj.getEdgeWeight(null)

        then:
        1 * graphMock.getEdgeWeight(null)
    }

    void 'verify delegation to void setEdgeWeight(E e, double weight)'() {
        when:
        testObj.setEdgeWeight(null, 0)

        then:
        1 * graphMock.setEdgeWeight(null, 0)
    }

}
