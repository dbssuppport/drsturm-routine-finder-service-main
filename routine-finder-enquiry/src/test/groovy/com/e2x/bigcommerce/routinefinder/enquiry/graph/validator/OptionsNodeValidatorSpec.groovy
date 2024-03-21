package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*
import spock.lang.Specification

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newConditionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionsVertex
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newRoutineFor

class OptionsNodeValidatorSpec extends Specification {

    void 'no errors are reported if an options can be added to a question'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()
        def questionNode = newQuestionVertexFor('age')
        routineGraph.add(questionNode)

        and:
        def vertexErrorReporting = Mock(RoutineGraphErrorReporting)
        def testObj = new OptionsNodeValidator(vertexErrorReporting)

        and:
        def options = newOptionsVertex()

        when:
        testObj.validateLinkVertex(options, questionNode.id, routineGraph)

        then:
        0 * vertexErrorReporting.errorOn(options, _)
    }

    void 'an error should be reported if an options node already exists on parent question node'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()
        def questionNode = newQuestionVertexFor('age')
        routineGraph.add(questionNode)
        routineGraph.link(questionNode, newOptionsVertex())

        and:
        def vertexErrorReporting = Mock(RoutineGraphErrorReporting)
        def testObj = new OptionsNodeValidator(vertexErrorReporting)

        and:
        def options = newOptionsVertex()

        when:
        testObj.validateLinkVertex(options, questionNode.id, routineGraph)

        then:
        1 * vertexErrorReporting.errorOn(options, _)
    }

    void 'an error should be reported if an options node is being attached to something else than a question'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()
        routineGraph.add(parentNode)

        and:
        def vertexErrorReporting = Mock(RoutineGraphErrorReporting)
        def testObj = new OptionsNodeValidator(vertexErrorReporting)

        and:
        def options = newOptionsVertex()

        when:
        testObj.validateLinkVertex(options, parentNode.id, routineGraph)

        then:
        1 * vertexErrorReporting.errorOn(options, _)

        where:
        parentNode << [newConditionFor("expr"), newOptionValueVertex('code'), newRoutineFor("steps"), newOptionsVertex()]
    }

}
