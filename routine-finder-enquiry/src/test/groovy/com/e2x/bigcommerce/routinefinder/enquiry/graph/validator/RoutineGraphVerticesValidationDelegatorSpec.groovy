package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator


import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*
import spock.lang.Specification
import spock.lang.Unroll

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionsVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newRoutineFor

@Unroll
class RoutineGraphVerticesValidationDelegatorSpec extends Specification {

    void 'it should delegate the validation for the given vertex'() {
        given:
        def testObj = RoutineGraphVerticesValidationFactory
                .newValidator()
                .with(Mock(OptionValueNodeRepository))
                .with(Mock(QuestionNodeRepository))
                .with(Mock(RoutineGraphErrorReporting))
                .instantiate()

        and:
        def mockValidator = Mock(VertexValidator)
        def graph = new InMemoryRoutineGraph()

        and:
        testObj.verticesValidatorMap[vertex.vertexType] = mockValidator

        when:
        testObj.validateVertex(vertex, graph)

        then:
        1 * mockValidator.validateVertex(vertex, graph)

        where:
        vertex << [
                newQuestionVertexFor('q1'),
                newConditionFor("string"),
                newRoutineFor("routine"),
                newOptionsVertex(),
                newOptionValueVertex('value')
        ]
    }
}
