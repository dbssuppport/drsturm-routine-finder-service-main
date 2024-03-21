package com.e2x.bigcommerce.routinefinder.enquiry

import spock.lang.Specification

class QuestionSpec extends Specification {

    void 'verify creation and equality'() {
        given:
        def question = new Question('id')

        expect:
        question.id == 'id'

        and:
        question == new Question(question.id)
        question == new Question('id')

        and:
        question != new Question('other')
    }
}
