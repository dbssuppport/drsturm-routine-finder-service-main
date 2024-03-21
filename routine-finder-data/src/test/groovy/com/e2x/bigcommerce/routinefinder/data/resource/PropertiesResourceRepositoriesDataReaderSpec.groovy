package com.e2x.bigcommerce.routinefinder.data.resource

import com.e2x.bigcommerce.routinefinder.data.SkuDefinition
import com.e2x.bigcommerce.routinefinder.data.repository.InMemorySkuDefinitionRepository
import spock.lang.Specification;

 class PropertiesResourceRepositoriesDataReaderSpec extends Specification {

     def "can load a sku definition from file"() {
         given:
         def propertiesResourceRepositoriesDataReader = new PropertiesResourceRepositoriesDataReader()
         def skuDefinitionRepository = new InMemorySkuDefinitionRepository()
         def resourceName = "sku-definition.properties"

         when:
         propertiesResourceRepositoriesDataReader.load(skuDefinitionRepository, resourceName)

         then:
         skuDefinitionRepository.findBy("", "body-cream").isPresent()
         skuDefinitionRepository.findBy("", "body-cream").get().getClass() == SkuDefinition
         skuDefinitionRepository.findBy("", "body-cream").get().getSkuId() == "08-100-04"
     }
}
