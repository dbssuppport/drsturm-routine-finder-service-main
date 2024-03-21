package com.e2x.bigcommerce.routinefinder.enquiry

import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder
import spock.lang.Specification

class QuestionnaireSpec extends Specification {

    void 'it should be outstanding if one or more questions are outstanding'() {
        given:
        def questionnaire = new Questionnaire()

        when:
        questionnaire.add(new Question(TestQuestionBuilder.AGE_ID, []))

        then:
        questionnaire.outstanding

        and:
        questionnaire.size() == 1

        when:
        questionnaire.findQuestionBy(TestQuestionBuilder.AGE_ID).get().answer = '1'

        then:
        !questionnaire.isOutstanding()

        when:
        questionnaire.add(new Question(TestQuestionBuilder.AGE_ID, [18]))
        questionnaire.add(new Question(TestQuestionBuilder.SKIN_TYPE_ID, []))

        then:
        questionnaire.outstanding

        and:
        questionnaire.size() == 2

        when:
        questionnaire.add(new Question(TestQuestionBuilder.AGE_ID, []))
        questionnaire.add(new Question(TestQuestionBuilder.SKIN_TYPE_ID, []))

        then:
        questionnaire.outstanding

        and:
        questionnaire.size() == 2
    }
}
