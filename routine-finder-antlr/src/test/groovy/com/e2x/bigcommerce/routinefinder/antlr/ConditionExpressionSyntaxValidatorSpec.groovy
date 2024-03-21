package com.e2x.bigcommerce.routinefinder.antlr

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ConditionExpressionSyntaxValidatorSpec extends Specification {

    void 'it reports an error if syntax is incorrect'() {
        given:
        def errorListener = Mock(ParsingErrorListener)

        and:
        def testObj = new ConditionExpressionSyntaxValidator()

        when:
        testObj.validate(expression, errorListener)

        then:
        1 * errorListener.error(_)

        where:
        expression << ['age', 'adsf', 'adsf is 2342342 asdf']
    }

    void 'it does not report errors on valid expression'() {
        given:
        def errorListener = Mock(ParsingErrorListener)

        and:
        def testObj = new ConditionExpressionSyntaxValidator()

        when:
        testObj.validate(expression, errorListener)

        then:
        0 * errorListener.error(_)

        where:
        expression << ['age is 18-25', 'skin tone is 7+', 'age in (18-24, 34-44)', 'age is not 18', 'age not in (1, 2)']
    }

    void 'it can handle list of values with white space'() {
        given:
        def errorListener = Mock(ParsingErrorListener)

        and:
        def testObj = new ConditionExpressionSyntaxValidator()

        when:
        testObj.validate('skin tone in (dark, very dark)', errorListener)

        then:
        0 * errorListener.error(_)
    }

    void 'it reports an error if value not allowed in list'() {
        given:
        def errorListener = Mock(ParsingErrorListener)

        and:
        def testObj = new ConditionExpressionSyntaxValidator()

        when:
        testObj.validate('skin tone in (dark, medium olive)', errorListener)

        then:
        1 * errorListener.error(_)
    }
}
