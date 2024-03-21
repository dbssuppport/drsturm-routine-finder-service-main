package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator


import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph
import com.e2x.bigcommerce.routinefinder.enquiry.graph.SkuNode
import com.e2x.bigcommerce.routinefinder.enquiry.graph.SkuNodeRepository
import spock.lang.Specification
import spock.lang.Unroll

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.*

@Unroll
class RoutineNodeValidatorSpec extends Specification {

    void 'routine nodes are only allowed on certain parent vertices'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        graph.add(parentVertex)
        def routineVertex = newRoutineFor('steps')
        def skuNodeRepository = Mock(SkuNodeRepository)

        and:
        def errorListener = Mock(RoutineGraphErrorReporting)
        def testObj = new RoutineNodeValidator(errorListener, skuNodeRepository)

        when:
        testObj.validateLinkVertex(routineVertex, parentVertex.getId(), graph)

        then:
        errorCount * errorListener.errorOn(routineVertex, _)

        where:
        errorCount  | parentVertex
        0           | newConditionFor('expr')
        0           | newQuestionVertexFor('age')
        1           | newOptionsVertex()
        1           | newOptionValueVertex('code')
        1           | newRoutineFor('other')
    }

    void 'a routine node must be of a allowed sku name'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        def errorListener = Mock(RoutineGraphErrorReporting)
        def skuNodeRepository = Mock(SkuNodeRepository)

        and:
        def routineVertex = newRoutineFor("step 1: " + skuName)

        and:
        skuNodeRepository.findNodeBy(skuName) >> skuDefinition

        and:
        def routineNodeValidator = new RoutineNodeValidator(errorListener, skuNodeRepository)

        when:
        routineNodeValidator.validateVertex(routineVertex, graph)

        then:
        errorCount * errorListener.errorOn(routineVertex, 'sku ' + skuName + ' does not exist')

        where:
        errorCount      | skuName           | skuDefinition
        0               | 'cleanser'        | Optional.of(new SkuNode())
        1               | 'other'           | Optional.empty()
    }

    void  'a routine node, with multiple steps, must validate each step as being an allowed sku name'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        def errorListener = Mock(RoutineGraphErrorReporting)
        def skuNodeRepository = Mock(SkuNodeRepository)

        and:
        def routineNodeValidator = new RoutineNodeValidator(errorListener, skuNodeRepository)

        and:
        def routineVertex = newRoutineFor("step 1: " + skuName1 +" step 2: " + skuName2)

        and:
        skuNodeRepository.findNodeBy(skuName1) >> skuDefinition1
        skuNodeRepository.findNodeBy(skuName2) >> skuDefinition2

        when:
        routineNodeValidator.validateVertex(routineVertex, graph)

        then:
        errorCountSku1 * errorListener.errorOn(routineVertex, 'sku '+ skuName1 +' does not exist')
        errorCountSku2 * errorListener.errorOn(routineVertex, 'sku ' + skuName2 + ' does not exist')

        where:
        errorCountSku1      | skuName1      | skuDefinition1                | errorCountSku2        | skuName2      | skuDefinition2
        0                   | 'cleanser'    | Optional.of(new SkuNode())    | 0                     | 'serum'       | Optional.of(new SkuNode())
        1                   | 'other'       | Optional.empty()              | 0                     | 'serum'       | Optional.of(new SkuNode())
        0                   | 'serum'       | Optional.of(new SkuNode())    | 1                     | 'other'       | Optional.empty()
        1                   | 'other'       | Optional.empty()              | 1                     | 'incorrect'   | Optional.empty()
    }

}
