package com.e2x.bigcommerce.routinefinder.cli.verification

import com.e2x.bigcommerce.routinefindermodel.Question
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import spock.lang.Specification

class RoutineFinderServiceScenarioRunnerSpec extends Specification {

    RoutineFinderServiceScenarioRunner testObj

    RoutineFinderClient routineFinderClient = Mock(RoutineFinderClient)

    void setup() {
        testObj = new RoutineFinderServiceScenarioRunner(routineFinderClient)
    }

    void 'it should create and submit an enquiry until completion'() {
        given:
        def scenario = Mock(Scenario)
        scenario.getSteps() >> []

        and:
        def routineEnquiry = Mock(RoutineEnquiry)
        routineEnquiry.getRoutines() >> []
        routineEnquiry.getQuestions() >> []

        and:
        routineFinderClient.startRoutineEnquiry() >> routineEnquiry

        and:
        routineEnquiry.isComplete() >> false >> false >> true

        when:
        testObj.execute(scenario)

        then:
        3 * routineFinderClient.submitRoutineEnquiry(routineEnquiry) >> routineEnquiry
    }

    void 'it should answer questions with scenario allowed choices for the given question'() {
        given:
        def allowedAgeChoices = Mock(AllowedChoices)
        allowedAgeChoices.getChoices() >> { ['expected-answer', 'unexpected-answer'] as Set }

        and:
        def scenario = Mock(Scenario)
        scenario.getSteps() >> []
        scenario.getAllowedChoicesFor('age') >> Optional.of(allowedAgeChoices)

        def routineEnquiry = Mock(RoutineEnquiry)
        routineEnquiry.getRoutines() >> []
        routineEnquiry.getQuestions() >> [new Question('age', null, null, null, null, [], null, null)]

        and:
        routineFinderClient.startRoutineEnquiry() >> routineEnquiry
        routineFinderClient.submitRoutineEnquiry(routineEnquiry) >> routineEnquiry

        and:
        routineEnquiry.isComplete() >> true

        when:
        testObj.execute(scenario)

        then:
        routineEnquiry.getQuestions()[0].answers == ['expected-answer']
    }
}
