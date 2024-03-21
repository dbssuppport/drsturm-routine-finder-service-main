package com.e2x.bigcommerce.routinefinder.data.resource

import com.e2x.bigcommerce.routinefinder.data.repository.InMemoryOptionDefinitionRepository
import com.e2x.bigcommerce.routinefinder.data.repository.InMemoryQuestionDefinitionRepository
import spock.lang.Specification

class PropertiesResourceRepositoriesDataReaderSpec extends Specification {

    PropertiesResourceRepositoriesDataReader testObj

    void setup() {
        testObj = new PropertiesResourceRepositoriesDataReader()
    }

    void 'it can load option definitions'() {
        given:
        def repository = new InMemoryOptionDefinitionRepository()

        when:
        testObj.load(repository, 'question-definition.properties')

        then:
        repository.findBy(null, null, '3 times').isPresent()
        repository.findBy(null, null, '3 times').get().text
    }

    void 'it can load question definitions'() {
        given:
        def repository = new InMemoryQuestionDefinitionRepository()

        when:
        testObj.load(repository, 'question-definition.properties')
        def optionalQuestionDefinition = repository.findBy(null, 'skin type')

        then:
        optionalQuestionDefinition.isPresent()

        and:
        def questionDefinition = optionalQuestionDefinition.get()
        questionDefinition.name
        questionDefinition.text
        questionDefinition.maxAllowedAnswers
    }
}
