package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator


import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*
import spock.lang.Specification

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionsVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newRoutineFor

class OptionValueNodeValidatorSpec extends Specification {

    OptionValueNodeValidator testObj

    OptionValueNodeRepository optionValueNodeRepository
    RoutineGraphErrorReporting vertexValidatorErrorReporting

    void setup() {
        optionValueNodeRepository = Mock(OptionValueNodeRepository)
        vertexValidatorErrorReporting = Mock(RoutineGraphErrorReporting)

        testObj = new OptionValueNodeValidator(vertexValidatorErrorReporting, optionValueNodeRepository)
    }

    void 'option code should be of a known list of options'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        optionValueNodeRepository.findNodeBy(code) >> expectedNode

        and:
        def optionValueNode = newOptionValueVertex(code)

        when:
        testObj.validateVertex(optionValueNode, graph)

        then:
        errorCount * vertexValidatorErrorReporting.errorOn(optionValueNode, _)

        where:
        errorCount  | code      | expectedNode
        0           | 'code'    | Optional.of(newOptionValueVertex('code'))
        1           | 'other'   | Optional.empty()
    }

    void 'option value node can only be attached to a options node'() {
        given:
        def graph = new InMemoryRoutineGraph()
        graph.add(parentVertex)

        and:
        def optionValueNode = newOptionValueVertex('code')

        when:
        testObj.validateLinkVertex(optionValueNode, parentVertex.getId(), graph)

        then:
        errorCount * vertexValidatorErrorReporting.errorOn(optionValueNode, _)

        where:
        errorCount  | parentVertex
        1           | newQuestionVertexFor('age')
        1           | newRoutineFor('steps')
        1           | newOptionValueVertex('code')
        1           | newConditionFor('expr')
        0           | newOptionsVertex()
    }

}
