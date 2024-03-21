package com.e2x.bigcommerce.routinefinder.data

import spock.lang.Specification

class SkuDefinitionSpec extends Specification {
    def "can access id, skuId and name"() {
        when:
        def skuDefinition = new SkuDefinition("123", "12354", "cleanser")

        then:
        skuDefinition.getId() == "123"
        skuDefinition.getName() == "cleanser"
        skuDefinition.getSkuId() == "12354"
    }
}
