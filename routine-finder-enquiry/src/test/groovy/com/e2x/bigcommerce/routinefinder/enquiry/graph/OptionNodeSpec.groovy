package com.e2x.bigcommerce.routinefinder.enquiry.graph

import com.e2x.bigcommerce.routinefinder.enquiry.QuestionnaireRoutineGraphImportInteropSpec
import com.e2x.bigcommerce.routinefinder.enquiry.QuestionnaireRoutineGraphService
import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.RoutineEnquiryBuilder
import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestGraphBuilder
import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.OptionNodeBuilder.*
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.RoutineEnquiryAnswerer.answer
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder.AGE
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder.SKIN_TYPE

class OptionNodeSpec extends QuestionnaireRoutineGraphImportInteropSpec {

    void setup() {
        testObj = new QuestionnaireRoutineGraphService()
    }

    void 'returns correct options based on answer for a question'() {
        given:
        routineGraph = TestGraphBuilder.questionWithOptionGraph()

        and:
        def routineEnquiry = RoutineEnquiryBuilder.startQuestionnaire()
        routineEnquiry = testObj.advance(routineGraph, routineEnquiry)

        and:
        answer(routineEnquiry, TestQuestionBuilder.AGE_ID, age)

        when:
        routineEnquiry = testObj.advance(routineGraph, routineEnquiry)

        then:
        routineEnquiry.findQuestionBy(TestQuestionBuilder.SKIN_TYPE_ID).get().options == expectedOptions

        where:
        age     | expectedOptions
        '26-35' | [LIGHT, DARK] as Set
        '18-25' | [LIGHT, DARK, VERY_DARK] as Set
    }
}
