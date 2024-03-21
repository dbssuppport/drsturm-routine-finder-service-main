package com.e2x.bigcommerce.routinefinderservice.service.routine

import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire
import com.e2x.bigcommerce.routinefindermodel.Question
import com.e2x.bigcommerce.routinefindermodel.Routine
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class RoutineEnquiryMapperSpec extends Specification {

    RoutineEnquiryMapper testObj

    QuestionMapper questionMapper = Mock()
    RoutineMapper routineMapper = Mock()

    void setup() {
        testObj = new RoutineEnquiryMapper(questionMapper, routineMapper)
    }

    void 'a questionnaire can be mapped into a routine enquiry'() {
        given:
        def questionnaire = Mock(Questionnaire)
        def storeId = 'a-store'

        and:
        def expectedQuestions = [Mock(Question)]
        questionMapper.mapFrom(storeId, questionnaire, _) >> expectedQuestions

        and:
        def expectedRoutines = [Mock(Routine)]
        routineMapper.mapFrom(storeId, questionnaire) >> expectedRoutines

        and:
        questionnaire.outstanding >> outstanding

        when:
        def result = testObj.mapFrom(storeId, questionnaire)

        then:
        result.complete == !outstanding
        result.questions == expectedQuestions

        and:
        result.routines == expectedRoutines

        where:
        outstanding << [true, false]
    }

}
