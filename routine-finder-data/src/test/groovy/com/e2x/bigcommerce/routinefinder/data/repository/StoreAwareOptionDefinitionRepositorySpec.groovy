package com.e2x.bigcommerce.routinefinder.data.repository

import spock.lang.Specification

class StoreAwareOptionDefinitionRepositorySpec extends Specification {

    StoreAwareOptionDefinitionRepository testObj

    void setup() {
        testObj = new StoreAwareOptionDefinitionRepository()
    }

    void 'it can load a default option definition from a question id and option code'() {
        when:
        def result = testObj.findBy('any-store', 'skin-type', 'dry')

        then:
        result
        result.isPresent()

        and:
        result.get().text
    }

    void 'it can return a different option definition for a different store'() {
        when:
        def result = testObj.findBy(storeId, 'skin-type', 'dry')

        then:
        result
        result.isPresent()

        and:
        result.get().text == expectedText

        where:
        storeId         | expectedText
        'bys9or2aai'    | 'Dry'
        'test'          | 'sec'
    }
}
