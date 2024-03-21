package com.e2x.bigcommerce.routinefinder.data.repository

import com.e2x.bigcommerce.routinefinder.data.SkuDefinition
import spock.lang.Specification

class InMemorySkuDefinitionRepositorySpec extends Specification {

    def "can save and find a sku definition"() {
        given:
        def skuDefinitionRepository = new InMemorySkuDefinitionRepository()
        def skuDefinition = new SkuDefinition("120", "120", "cleanser")

        when:
        skuDefinitionRepository.save(skuDefinition)
        def retrievedSku = skuDefinitionRepository.findBy("", "cleanser")

        then:
        retrievedSku.present
        retrievedSku == Optional.of(skuDefinition)
        retrievedSku.get() == skuDefinition
    }

    def "when name cannot be found it returns empty"() {
        given:
        def skuDefinitionRepository = new InMemorySkuDefinitionRepository()
        def skuDefinition = new SkuDefinition("", "", "cleanser")

        when:
        skuDefinitionRepository.save(skuDefinition)
        def retrievedSku =  skuDefinitionRepository.findBy("", "incorrect name")

        then:
        retrievedSku.empty
    }

    def 'sku can be found regardless of case used when saving or finding'() {
        given:
        def skuDefinitionRepository = new InMemorySkuDefinitionRepository()
        def skuDefinition = new SkuDefinition("", "", skuToSave)

        when:
        skuDefinitionRepository.save(skuDefinition)
        def retrievedSku =  skuDefinitionRepository.findBy("", skuNameToFind)

        then:
        retrievedSku.present
        retrievedSku.get() == skuDefinition

        where:
        skuToSave           | skuNameToFind
        "cleanser"          | "CLEANSER"
        "CLEANSER"          | "cleanser"
    }

}
