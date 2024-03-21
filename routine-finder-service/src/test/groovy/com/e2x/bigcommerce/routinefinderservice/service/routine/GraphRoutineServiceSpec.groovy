package com.e2x.bigcommerce.routinefinderservice.service.routine

import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire
import com.e2x.bigcommerce.routinefinder.enquiry.QuestionnaireRoutineGraphService
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import spock.lang.Specification

class GraphRoutineServiceSpec extends Specification {

    GraphRoutineService testObj

    QuestionnaireRoutineGraphService questionnaireRoutineGraphService
    RoutineGraphRepository routineGraphRepository
    QuestionnaireMapper questionnaireMapper
    RoutineEnquiryMapper routineEnquiryMapper

    void setup() {
        routineGraphRepository = Mock()
        questionnaireRoutineGraphService = Mock()
        questionnaireMapper = Mock()
        routineEnquiryMapper = Mock()

        testObj = new GraphRoutineService(routineGraphRepository, questionnaireRoutineGraphService, routineEnquiryMapper, questionnaireMapper)
    }

    void 'a new routine is returned when starting a routine enquiry'() {
        given:
        def routineGraph = Mock(RoutineGraph)
        routineGraphRepository.fetchCurrent() >> routineGraph

        and:
        def questionnaire = Mock(Questionnaire)
        questionnaireRoutineGraphService.advance(routineGraph, _) >> questionnaire

        and:
        def expectedRoutine = Mock(RoutineEnquiry)
        routineEnquiryMapper.mapFrom('a-store', questionnaire) >> expectedRoutine

        when:
        def result = testObj.start('a-store')

        then:
        result == expectedRoutine
    }

    void 'an updated routine is returned when advancing a routine enquiry'() {
        given:
        def routineEnquiry = Mock(RoutineEnquiry)

        and:
        def questionnaire = Mock(Questionnaire)
        questionnaireMapper.mapFrom(routineEnquiry) >> questionnaire

        and:
        def routineGraph = Mock(RoutineGraph)
        routineGraphRepository.fetchCurrent() >> routineGraph

        and:
        def advancedQuestionnaire = Mock(Questionnaire)
        questionnaireRoutineGraphService.advance(routineGraph, questionnaire) >> advancedQuestionnaire

        and:
        def expectedRoutineEnquiry = Mock(RoutineEnquiry)
        routineEnquiryMapper.mapFrom('a-store', advancedQuestionnaire, routineEnquiry) >> expectedRoutineEnquiry

        when:
        def result = testObj.submit('a-store', routineEnquiry)

        then:
        result == expectedRoutineEnquiry
    }

}
