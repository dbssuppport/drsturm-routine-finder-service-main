package com.e2x.bigcommerce.routinefinderservice.configuration


import com.e2x.bigcommerce.routinefinderservice.service.routine.GraphRoutineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static com.e2x.bigcommerce.routinefinderservice.TestUtils.answer

/**
 * temporary tests to show question navigations etc...
 */
@SpringBootTest
@ActiveProfiles('integration')
class TestGraphSpec extends Specification {

    @Autowired
    GraphRoutineService graphRoutineService

    void 'execute a routine'() {
        when:
        def routineEnquiry = graphRoutineService.start("")

        then:
        !routineEnquiry.isComplete()

        and:
        routineEnquiry.questions

        when:
        answer(routineEnquiry, QuestionIds.AGE, '18-24')

        and:
        routineEnquiry = graphRoutineService.submit('a-store', routineEnquiry)

        then:
        !routineEnquiry.isComplete()

        and:
        routineEnquiry.routines

        when:
        answer(routineEnquiry, QuestionIds.SKIN_TYPE, 'dry')
        answer(routineEnquiry, QuestionIds.SKIN_TONE, 'light')
        answer(routineEnquiry, QuestionIds.DAILY_USAGE, '4')

        and:
        routineEnquiry = graphRoutineService.submit('a-store', routineEnquiry)

        then:
        routineEnquiry.isComplete()

        and:
        routineEnquiry.routines
        routineEnquiry.routines.size() > 0

        when:
        answer(routineEnquiry, QuestionIds.AGE, '25-34')

        and:
        routineEnquiry = graphRoutineService.submit('a-store', routineEnquiry)

        then:
        !routineEnquiry.isComplete()

        and:
        !routineEnquiry.routines

        and:
        routineEnquiry.getQuestions().find { q -> q.id == QuestionIds.SKIN_CONCERNS }

        and:
        routineEnquiry.getQuestions().find({ q-> q.id == QuestionIds.SKIN_CONCERNS }).options*.code == ['pollution', 'sun']

        when:
        answer(routineEnquiry, QuestionIds.AGE, '35-44')

        and:
        routineEnquiry = graphRoutineService.submit('a-store', routineEnquiry)

        then:
        routineEnquiry.getQuestions().find({ q-> q.id == QuestionIds.SKIN_CONCERNS }).options*.code == ['pollution', 'sun', 'wrinkles']
    }

}
