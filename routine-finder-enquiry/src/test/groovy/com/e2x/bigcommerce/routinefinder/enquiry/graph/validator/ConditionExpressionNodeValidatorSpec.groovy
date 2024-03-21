package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting

import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph

import spock.lang.Specification
import spock.lang.Unroll

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionsVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newRoutineFor

@Unroll
class ConditionExpressionNodeValidatorSpec extends Specification {

    ConditionExpressionNodeValidator testObj

    RoutineGraphErrorReporting errorReporter
    ConditionExpressionValidator conditionExpressionValidator

    void setup() {
        errorReporter = Mock(RoutineGraphErrorReporting)
        conditionExpressionValidator = Mock(ConditionExpressionValidator)

        testObj = new ConditionExpressionNodeValidator(errorReporter, conditionExpressionValidator)
    }

    void 'condition linked to nodes other than condition or question should report an error'() {
        given:
        def graph = new InMemoryRoutineGraph()
        graph.add(parentNode)

        and:
        def conditionVertex = newConditionFor('expr')

        when:
        testObj.validateLinkVertex(conditionVertex, parentNode.getId(), graph)

        then:
        1 * errorReporter.errorOn(conditionVertex, _)

        where:
        parentNode << [newOptionsVertex(), newOptionValueVertex('code'), newRoutineFor('steps')]
    }

    void 'condition linked to allowed parent vertex should not report an error'() {
        given:
        def graph = new InMemoryRoutineGraph()
        graph.add(parentNode)

        and:
        def conditionVertex = newConditionFor('expr')

        when:
        testObj.validateLinkVertex(conditionVertex, parentNode.getId(), graph)

        then:
        0 * errorReporter.errorOn(conditionVertex, _)

        where:
        parentNode << [newQuestionVertexFor('age'), newConditionFor('expr')]
    }

    void 'condition with the same expression is already linked to the parent vertex'() {
        given:
        def graph = new InMemoryRoutineGraph()
        graph.add(parentNode)
        graph.link(parentNode, newConditionFor('expr'))

        and:
        def conditionVertex = newConditionFor('expr')

        when:
        testObj.validateLinkVertex(conditionVertex, parentNode.getId(), graph)

        then:
        1 * errorReporter.errorOn(conditionVertex, _)

        where:
        parentNode << [newConditionFor('other'), newQuestionVertexFor('age')]
    }

    void 'condition with different expression to the parent vertex should not error new condition to be linked to the same parent'() {
        given:
        def graph = new InMemoryRoutineGraph()
        graph.add(parentNode)
        graph.link(parentNode, newConditionFor('other expr'))

        and:
        def conditionVertex = newConditionFor('expr')

        when:
        testObj.validateLinkVertex(conditionVertex, parentNode.getId(), graph)

        then:
        0 * errorReporter.errorOn(conditionVertex, _)

        where:
        parentNode << [newConditionFor('other'), newQuestionVertexFor('age')]
    }

    void 'it should validate expressions and report errors or not'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        def conditionVertex = newConditionFor(expression)

        when:
        testObj.validateVertex(conditionVertex, graph)

        then:
        errorCount * errorReporter.errorOn(conditionVertex, _)

        where:
        errorCount  | expression
        0           | 'age is 18'
        1           | 'garbage text'
    }

}
