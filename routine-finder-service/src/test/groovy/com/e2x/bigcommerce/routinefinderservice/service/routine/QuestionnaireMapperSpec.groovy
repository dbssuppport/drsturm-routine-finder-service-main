package com.e2x.bigcommerce.routinefinderservice.service.routine

import com.e2x.bigcommerce.routinefindermodel.Question
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import spock.lang.Specification

class QuestionnaireMapperSpec extends Specification {

    QuestionnaireMapper testObj

    void setup() {
        testObj = new QuestionnaireMapper()
    }

    void 'can map questions from routine enquiry into questionnaire'() {
        given:
        def routineEnquiry = new RoutineEnquiry()
        routineEnquiry.questions = [new Question(id: 1, answers: ['an-answer'])]

        when:
        def result = testObj.mapFrom(routineEnquiry)

        then:
        result.size() == 1
        result.first().id == '1'
        result.first().answers == ['an-answer']
    }

    void 'can map multiple questions from routine enquiry into questionnaire'() {
        given:
        def routineEnquiry = new RoutineEnquiry()
        routineEnquiry.questions = [new Question(id: 1, answers: ['an-answer']), new Question(id: 2, answers: ['another-answer'])]

        when:
        def result = testObj.mapFrom(routineEnquiry)

        then:
        result.size() == 2

        and:
        result.contains(new com.e2x.bigcommerce.routinefinder.enquiry.Question('1'))
        result.contains(new com.e2x.bigcommerce.routinefinder.enquiry.Question('2'))
    }
}
