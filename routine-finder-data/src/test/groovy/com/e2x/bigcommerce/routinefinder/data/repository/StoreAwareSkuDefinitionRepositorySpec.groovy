package com.e2x.bigcommerce.routinefinder.data.repository

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class StoreAwareSkuDefinitionRepositorySpec extends Specification {

    StoreAwareSkuDefinitionRepository testObj

    void setup() {
        testObj = new StoreAwareSkuDefinitionRepository()
    }

    void 'it can load a default sku from a name'() {
        when:
        def result = testObj.findBy('any-store', skuName)

        then:
        result
        result.isPresent()

        and:
        result.get().skuId
        result.get().name == skuName.toLowerCase()

        where:
        skuName << ['body-cream', 'Body-Cream']
    }

    void 'it can return a different sku id for a different store'() {
        when:
        def result = testObj.findBy(storeId, skuName)

        then:
        result
        result.isPresent()

        and:
        result.get().skuId == expectedSkuId
        result.get().name == skuName.toLowerCase()

        where:
        storeId         | skuName       | expectedSkuId
        'bys9or2aai'    | 'body-cream'  | '08-100-04'
        'test'          | 'body-cream'  | '08-100-BOD-test'
    }
}
