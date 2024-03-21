package com.e2x.bigcommerce.routinefinder.antlr

import spock.lang.Specification

class RoutineFinderParserSpec extends Specification {

    void 'verify "in" operand'() {
        given:
        def questionFinder = Mock(QuestionAnswerFinder)
        questionFinder.findAnswersFor('age') >> answers

        and:
        def expression = new RoutineFinderExpressionInterpreter()

        when:
        def result = expression.evaluate('age in (1, 2)', questionFinder)

        then:
        result == expected

        where:
        expected    | answers
        true        | [1]
        true        | [2]
        false       | [3]
        true        | ['1']
        true        | ['2']
        false       | ['3']
        false       | null
        false       | []
    }

    void 'verify "not in" operand'() {
        given:
        def questionFinder = Mock(QuestionAnswerFinder)
        questionFinder.findAnswersFor('age') >> answers

        and:
        def interpreter = new RoutineFinderExpressionInterpreter()

        when:
        def result = interpreter.evaluate('age not in (1, 2)', questionFinder)

        then:
        result == expected

        where:
        expected    | answers
        false       | [1]
        false       | [2]
        false       | ['1']
        false       | ['2']
        true        | [3]
        true        | ['3']
        false       | []
        false       | null
    }

    void 'verify "is" operand'() {
        given:
        def questionFinder = Mock(QuestionAnswerFinder)
        questionFinder.findAnswersFor('age') >> age

        and:
        def interpreter = new RoutineFinderExpressionInterpreter()

        when:
        def result = interpreter.evaluate('age is 1', questionFinder)

        then:
        result == expected

        where:
        expected    | age
        true        | [1]
        false       | [2]
        true        | ['1']
        false       | null
        false       | []
    }

    void 'verify "is not" operand'() {
        given:
        def questionFinder = Mock(QuestionAnswerFinder)
        questionFinder.findAnswersFor('age') >> age

        and:
        def interpreter = new RoutineFinderExpressionInterpreter()

        when:
        def result = interpreter.evaluate('age is not 1', questionFinder)

        then:
        result == expected

        where:
        expected    | age
        false       | [1]
        true        | [2]
        true        | ['2']
        false       | []
        false       | null
    }

    void 'an exception should be thrown if a question does not exists'() {
        given:
        def questionFinder = Mock(QuestionAnswerFinder)

        and:
        def interpreter = new RoutineFinderExpressionInterpreter()

        when:
        interpreter.evaluate(expression, questionFinder)

        then:
        thrown ConditionExpressionException

        where:
        expression << ['something is not 1', 'something is 1', 'something in (1)', 'something not in (1)']
    }

    void 'an exception should be thrown if an operand does not exists'() {
        given:
        def questionFinder = Mock(QuestionAnswerFinder)

        and:
        def interpreter = new RoutineFinderExpressionInterpreter()

        when:
        interpreter.evaluate(expression, questionFinder)

        then:
        thrown ConditionExpressionException

        where:
        expression << ['age something 1', 'something age (1)']
    }

    void 'an exception should be thrown if incorrect values'() {
        given:
        def questionFinder = Mock(QuestionAnswerFinder)

        and:
        def interpreter = new RoutineFinderExpressionInterpreter()

        when:
        interpreter.evaluate(expression, questionFinder)

        then:
        thrown ConditionExpressionException

        where:
        expression << ['age is (1, 2)', 'age in 1']
    }

}
