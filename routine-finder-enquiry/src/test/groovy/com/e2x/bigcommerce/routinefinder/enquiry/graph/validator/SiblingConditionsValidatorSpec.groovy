package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator

import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph
import spock.lang.Specification
import spock.lang.Unroll

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor

@Unroll
class SiblingConditionsValidatorSpec extends Specification {

    SiblingConditionsValidator testObj

    void setup() {
        testObj = new SiblingConditionsValidator()
    }

    void 'when an existing sibling contains a condition which would conflict with existing one'() {
        given:
        def graph = new InMemoryRoutineGraph()
        def question = newQuestionVertexFor('parent-id')
        graph.add(question)

        and:
        def sibling = newConditionFor(siblingExpr)
        graph.add(sibling)
        graph.link(question, sibling)

        and:
        def condition = newConditionFor(conditionExpr)
        graph.add(condition)

        when:
        def result = testObj.conflictingExpressionPresent(condition, question, graph)

        then:
        result

        where:
        siblingExpr             | conditionExpr
        'age in (one)'          | 'age in (one, two)'
        'age not in (one)'      | 'age in (two)'
        'age not in (one, two)' | 'age not in (two)'
        'age is one'            | 'age not in (two)'
        'age is one'            | 'age in (one, two)'
        'age is not one'        | 'age not in (one, two)'
    }

    void 'return false when no existing siblings that contains a condition which conflicts'() {
        given:
        def graph = new InMemoryRoutineGraph()
        def question = newQuestionVertexFor('parent-id')
        graph.add(question)

        and:
        def condition = newConditionFor('age in (one, two)')
        graph.add(condition)

        when:
        def result = testObj.conflictingExpressionPresent(condition, question, graph)

        then:
        !result
    }

    void 'return false when not one existing siblings that contains a condition which conflicts'() {
        given:
        def graph = new InMemoryRoutineGraph()
        def question = newQuestionVertexFor('parent-id')
        graph.add(question)

        and:
        def sibling = newConditionFor(conditionExpr)
        graph.add(sibling)
        graph.link(question, sibling)

        and:
        def condition = newConditionFor(siblingExpr)
        graph.add(condition)

        when:
        def result = testObj.conflictingExpressionPresent(condition, question, graph)

        then:
        !result

        where:
        conditionExpr               | siblingExpr
        'age in (three)'            | 'age in (one, two)'
        'age is one'                | 'age not in (one, two)'
        'age is not one'            | 'age in (one, two)'
        'age is one'                | 'age is not one'
        'age not in (one)'          | 'age not in (two)'
    }

    void 'return true when at least one existing siblings contains a condition which conflicts'() {
        given:
        def graph = new InMemoryRoutineGraph()
        def question = newQuestionVertexFor('parent-id')
        graph.add(question)

        and:
        def sibling = newConditionFor('age in (three)')
        graph.add(sibling)
        graph.link(question, sibling)

        and:
        def otherSibling = newConditionFor('age in (one)')
        graph.add(otherSibling)
        graph.link(question, otherSibling)

        and:
        def condition = newConditionFor('age in (one, two)')
        graph.add(condition)

        when:
        def result = testObj.conflictingExpressionPresent(condition, question, graph)

        then:
        result
    }

}
