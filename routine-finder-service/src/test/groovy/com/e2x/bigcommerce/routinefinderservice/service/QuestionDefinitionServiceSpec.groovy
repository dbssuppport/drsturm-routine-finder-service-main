package com.e2x.bigcommerce.routinefinderservice.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles('integration')
class QuestionDefinitionServiceSpec extends Specification {

    @Autowired
    QuestionDefinitionService testObj

    void 'returns text for a question'() {
        expect:
        testObj.getQuestionTextFor("", 'age') == 'How old are you?'
    }

    void 'returns option text for a question option'() {
        expect:
        testObj.getOptionTextFor("", 'age', '18-24') == '18 - 24'
    }

    void 'returns max allowed answers for a question'() {
        expect:
        testObj.getMaxAllowedAnswerFor("", 'age') == 1
    }

    void 'replaces blank with dashes on ids'() {
        expect:
        testObj.getQuestionTextFor("", 'skin concerns') == 'What are your top skincare concerns?'
        testObj.getOptionTextFor("", 'skin concerns', 'dry skin') == 'Dry skin'
        testObj.getMaxAllowedAnswerFor("", 'skin concerns') == 3
    }

    void 'returns type - text for age question'() {
        expect:
        testObj.getQuestionTypeFor("", 'age') == 'text'
    }

    void 'returns type - icon for skin type question'() {
        expect:
        testObj.getQuestionTypeFor("", 'skin type') == 'icon'
    }

    void 'returns type - slider for a skin tone question'() {
        expect:
        testObj.getQuestionTypeFor("", 'skin tone') == 'slider'
    }
}
