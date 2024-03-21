package com.e2x.bigcommerce.routinefinder.enquiry.graph


import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class QuestionnaireAwareRoutineGraphSpec extends Specification {

    QuestionnaireAwareRoutineGraph testObj

    Questionnaire questionnaireMock
    RoutineGraph delegatedGraphMock

    void setup() {
        questionnaireMock = Mock()
        delegatedGraphMock = Mock()

        testObj = new QuestionnaireAwareRoutineGraph(questionnaireMock, delegatedGraphMock)
    }

    void 'it should return any edges associated to a triggered vertex'() {
        given:
        def vertex = rootVertex(true)

        and:
        def edges = [edge(), edge()] as Set<Edge>

        and:
        delegatedGraphMock.outgoingEdgesOf(vertex) >> edges

        when:
        def triggeredEdges = testObj.outgoingEdgesOf(vertex)

        then:
        triggeredEdges == edges
    }

    void 'it should order outgoing edges so that the vertex type with highest priority comes out first'() {
        given:
        def vertex = rootVertex(true)

        and:
        def edges = [edge(mockedVertex(first)), edge(mockedVertex(second)), edge(mockedVertex(third))] as Set<Edge>

        and:
        delegatedGraphMock.outgoingEdgesOf(vertex) >> edges

        when:
        def triggeredEdges = testObj.outgoingEdgesOf(vertex)
        def triggeredVertices = triggeredEdges.collect {
            delegatedGraphMock.getEdgeTarget(it)
        }

        then:
        (triggeredVertices*.vertexType).last() == VertexType.ROUTINE

        where:
        first                   | second                | third
        VertexType.ROUTINE      | VertexType.CONDITION  | VertexType.QUESTION
        VertexType.CONDITION    | VertexType.ROUTINE    | VertexType.QUESTION
        VertexType.QUESTION     | VertexType.ROUTINE    | VertexType.CONDITION
        VertexType.CONDITION    | VertexType.QUESTION   | VertexType.ROUTINE
        VertexType.QUESTION     | VertexType.CONDITION  | VertexType.ROUTINE
    }

    void 'it should return empty set if non triggered'() {
        given:
        def vertex = rootVertex(false)

        and:
        def edges = [edge()] as Set<Edge>

        and:
        delegatedGraphMock.outgoingEdgesOf(vertex) >> edges

        when:
        def triggeredEdges = testObj.outgoingEdgesOf(vertex)

        then:
        triggeredEdges == [] as Set<Edge>
    }

    void 'returns empty set if no root found on graph'() {
        given:
        def vertex = Mock(Vertex)
        delegatedGraphMock.findRoots() >> [vertex]

        expect:
        testObj.findRoots() == [vertex] as Set
    }

    void 'can find all roots of this graph'() {
        given:
        def expectedVertex1 = rootVertex()

        and:
        delegatedGraphMock.findRoots() >> [expectedVertex1]

        expect:
        testObj.findRoots() == [expectedVertex1] as Set
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

    private Edge edge(Vertex target = null) {
        def edge = Mock(Edge)
        delegatedGraphMock.getEdgeTarget(edge) >> target
        edge
    }

    private Vertex mockedVertex(VertexType vertexType) {
        def vertex = Mock(Vertex)
        vertex.getVertexType() >> vertexType
        vertex
    }
}
