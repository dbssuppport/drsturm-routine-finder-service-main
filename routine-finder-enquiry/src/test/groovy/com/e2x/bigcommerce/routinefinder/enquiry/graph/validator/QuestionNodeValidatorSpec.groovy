package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator


import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*
import spock.lang.Specification
import spock.lang.Unroll

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionsVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newRoutineFor

@Unroll
class QuestionNodeValidatorSpec extends Specification {

    QuestionNodeValidator testObj

    RoutineGraphErrorReporting errorReporting
    QuestionNodeRepository questionNodeRepository

    void setup() {
        errorReporting = Mock(RoutineGraphErrorReporting)
        questionNodeRepository = Mock(QuestionNodeRepository)

        testObj = new QuestionNodeValidator(errorReporting, questionNodeRepository)
    }

    void 'a question node must be of a allowed question id'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        questionNodeRepository.findNodeBy(questionId) >> questionNode

        and:
        def vertex = newQuestionVertexFor(questionId)

        when:
        testObj.validateVertex(vertex, graph)

        then:
        errorCount * errorReporting.errorOn(vertex, _)

        where:
        errorCount      | questionId      | questionNode
        0               | 'age'           | Optional.of(newQuestionVertexFor('age'))
        1               | 'other'         | Optional.empty()
    }

    void 'a question can only be attached to a question, condition or be root'() {
        given:
        def graph = new InMemoryRoutineGraph()
        graph.add(parentVertex)
        def questionNode = newQuestionVertexFor('skin type')

        when:
        testObj.validateLinkVertex(questionNode, parentVertex.id, graph)

        then:
        errorCount * errorReporting.errorOn(questionNode, _)

        where:
        errorCount | parentVertex
        0          | newQuestionVertexFor('age')
        0          | newConditionFor('expr')
        1          | newOptionsVertex()
        1          | newOptionValueVertex('value')
        1          | newRoutineFor('value')
    }

    void 'a question id cannot be linked more than once in the path to root'() {
        given:
        def graph = new InMemoryRoutineGraph()
        def questionId = 'age'

        and:
        def aVertex = new Vertex()
        def questionVertex = newQuestionVertexFor(questionId)
        def sameQuestionVertex = newQuestionVertexFor(questionId)

        and:
        graph.add(aVertex)
        graph.add(sameQuestionVertex)
        graph.link(aVertex, sameQuestionVertex)

        and:
        def leafVertex = graph.add(newQuestionVertexFor('other'))
        graph.link(sameQuestionVertex, leafVertex)

        when:
        testObj.validateLinkVertex(questionVertex, leafVertex.id, graph)

        then:
        1 * errorReporting.errorOn(questionVertex, _)
    }

}
