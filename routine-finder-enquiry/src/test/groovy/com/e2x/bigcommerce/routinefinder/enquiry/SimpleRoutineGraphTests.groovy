package com.e2x.bigcommerce.routinefinder.enquiry


import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.RoutineEnquiryBuilder
import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestGraphBuilder
import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder
import spock.lang.Shared
import spock.lang.Stepwise

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.RoutineEnquiryAnswerer.answer

@Stepwise
class SimpleRoutineGraphTests extends QuestionnaireRoutineGraphImportInteropSpec {

    @Shared
    Questionnaire questionnaire

    void setupSpec() {
        routineGraph = TestGraphBuilder.aRoutineGraph()

        questionnaire = RoutineEnquiryBuilder.startQuestionnaire()
        testObj = new QuestionnaireRoutineGraphService()
    }

    void 'when starting with an empty routine it should return a list of questions to answer'() {
        when:
        questionnaire = testObj.advance(routineGraph, questionnaire)

        then:
        questionnaire.size() == 2

        and:
        questionnaire[0].id == TestQuestionBuilder.AGE_ID
        questionnaire[1].id == TestQuestionBuilder.SKIN_TYPE_ID

        and:
        questionnaire.routines != null
        questionnaire.routines.size() == 0
    }

    void 'when answering one question it should still return one question to answer'() {
        given:
        answer(questionnaire, TestQuestionBuilder.AGE_ID, '18-25')

        when:
        questionnaire = testObj.advance(routineGraph, questionnaire)

        then:
        questionnaire.size() == 2

        and:
        questionnaire[0].id == TestQuestionBuilder.AGE_ID
        questionnaire[1].id == TestQuestionBuilder.SKIN_TYPE_ID

        and:
        !questionnaire[0].isOutstanding()

        and:
        questionnaire[1].isOutstanding()
    }

    void 'when answering age triggering simple routine path and all questions answered then simple routine is returned'() {
        given:
        answer(questionnaire, TestQuestionBuilder.SKIN_TYPE_ID, 'light')

        when:
        questionnaire = testObj.advance(routineGraph, questionnaire)

        then:
        questionnaire.size() == 2

        and:
        questionnaire.every { !it.outstanding }

        and:
        questionnaire.routines != null
        questionnaire.routines.first()
        questionnaire.routines.first().steps*.skuId == ['simple-cleanser']
    }

    void 'when answering age triggering medium routine path and all questions answered then medium routine is returned'() {
        given:
        answer(questionnaire, TestQuestionBuilder.AGE_ID, '26-35')

        when:
        questionnaire = testObj.advance(routineGraph, questionnaire)

        then:
        questionnaire.size() == 2

        and:
        questionnaire.every { !it.outstanding }

        and:
        questionnaire.routines != null
        questionnaire.routines.first()
        questionnaire.routines.first().steps*.skuId == ['medium-cleanser']
    }

}
