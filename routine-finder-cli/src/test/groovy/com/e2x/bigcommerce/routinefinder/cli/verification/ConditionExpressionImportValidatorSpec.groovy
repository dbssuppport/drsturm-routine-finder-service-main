package com.e2x.bigcommerce.routinefinder.cli.verification

import com.e2x.bigcommerce.routinefinder.data.OptionDefinition
import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinition
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository
import spock.lang.Specification

class ConditionExpressionImportValidatorSpec extends Specification {
    private static final String DEFAULT = "default"

    ConditionExpressionImportValidator testObj

    QuestionDefinitionRepository questionDefinitionRepository
    OptionDefinitionRepository optionDefinitionRepository

    void setup() {
        questionDefinitionRepository = Mock(QuestionDefinitionRepository)
        optionDefinitionRepository = Mock(OptionDefinitionRepository)

        testObj = new ConditionExpressionImportValidator(questionDefinitionRepository, optionDefinitionRepository)
    }

    void 'does not raise an error if question used in condition expression does exists in definition repository'() {
        given:
        def reportedError = null

        and:
        questionDefinitionRepository.findBy(DEFAULT, 'age') >> Optional.of(Mock(QuestionDefinition))
        optionDefinitionRepository.findBy(DEFAULT, null, '18') >> Optional.of(Mock(OptionDefinition))

        when:
        testObj.validate('age is 18', { errorMessage -> reportedError = errorMessage })

        then:
        !reportedError
    }

    void 'raises an error if question used in condition expression does not exists in definition repository'() {
        given:
        def reportedError = null

        and:
        questionDefinitionRepository.findBy(DEFAULT, 'age') >> Optional.empty()

        when:
        testObj.validate('age is 18', { errorMessage -> reportedError = errorMessage })

        then:
        reportedError
    }

    void 'raise an error if the option used in an "is" match does not exists in definition repository'() {
        given:
        def reportedError = null

        and:
        questionDefinitionRepository.findBy(DEFAULT, 'age') >> Optional.of(Mock(QuestionDefinition))
        optionDefinitionRepository.findBy(DEFAULT, null, '18') >> Optional.empty()

        when:
        testObj.validate('age is 18', { errorMessage -> reportedError = errorMessage })

        then:
        reportedError
    }

    void 'raise an error if one of the option used in an "in" match does not exists in definition repository'() {
        given:
        def reportedError = null

        and:
        questionDefinitionRepository.findBy(DEFAULT, 'age') >> Optional.of(Mock(QuestionDefinition))
        optionDefinitionRepository.findBy(DEFAULT, null, '18') >> Optional.of(Mock(OptionDefinition))
        optionDefinitionRepository.findBy(DEFAULT, null, '19') >> Optional.empty()

        when:
        testObj.validate('age in (18, 19)', { errorMessage -> reportedError = errorMessage })

        then:
        reportedError
    }

    void 'does not raise an error if all of the options used in an "in" match exists in definition repository'() {
        given:
        def reportedError = null

        and:
        questionDefinitionRepository.findBy(DEFAULT, 'age') >> Optional.of(Mock(QuestionDefinition))
        optionDefinitionRepository.findBy(DEFAULT, null, '18') >> Optional.of(Mock(OptionDefinition))
        optionDefinitionRepository.findBy(DEFAULT, null, '19') >> Optional.of(Mock(OptionDefinition))

        when:
        testObj.validate('age in (18, 19)', { errorMessage -> reportedError = errorMessage })

        then:
        !reportedError
    }
}
