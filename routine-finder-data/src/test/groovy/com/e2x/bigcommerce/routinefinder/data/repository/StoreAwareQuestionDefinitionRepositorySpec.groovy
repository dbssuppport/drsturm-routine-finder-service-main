package com.e2x.bigcommerce.routinefinder.data.repository

import spock.lang.Specification

class StoreAwareQuestionDefinitionRepositorySpec extends Specification {

    StoreAwareQuestionDefinitionRepository testObj

    void setup() {
        testObj = new StoreAwareQuestionDefinitionRepository()
    }

    void 'it can load a default question definition from a question id'() {
        when:
        def result = testObj.findBy('any-store', questionId)

        then:
        result
        result.isPresent()

        and:
        result.get().getId() == questionId
        result.get().name

        where:
        questionId << ['age', 'skin-tone']
    }

    void 'it can return a different question definition for a different store'() {
        when:
        def result = testObj.findBy(storeId, questionId)

        then:
        result
        result.isPresent()

        and:
        result.get().text == expectedText

        where:
        storeId         | questionId   | expectedText
        //'bys9or2aai'    | 'skin-type'  | 'How would you describe your skin type?'
        'test'          | 'skin-type'  | 'A different text for test store'
    }
}
