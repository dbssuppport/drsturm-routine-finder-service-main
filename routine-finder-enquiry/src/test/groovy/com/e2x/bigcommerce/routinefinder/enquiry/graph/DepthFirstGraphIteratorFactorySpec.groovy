package com.e2x.bigcommerce.routinefinder.enquiry.graph

import org.jgrapht.traverse.DepthFirstIterator
import spock.lang.Specification

class DepthFirstGraphIteratorFactorySpec extends Specification {

    DepthFirstGraphIteratorFactory testObj
    QuestionnaireAwareRoutineGraph graphMock

    void setup() {
        testObj = new DepthFirstGraphIteratorFactory()

        graphMock = Mock()
    }

    void 'creates a depth first routine graph iterator using the root vertex as starting point'() {
        given:
        def rootVertex = Mock(Vertex)

        and:
        graphMock.findRoots() >> [rootVertex]
        graphMock.containsVertex(rootVertex) >> true

        when:
        def iterator = testObj.createIterator(graphMock)

        then:
        iterator

        and:
        iterator instanceof DepthFirstIterator

        and:
        iterator.hasNext()

        and:
        ((DepthFirstIterator)iterator).stack.first() == rootVertex
    }
}
