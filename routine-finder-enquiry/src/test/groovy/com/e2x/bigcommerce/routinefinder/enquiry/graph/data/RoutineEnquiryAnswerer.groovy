package com.e2x.bigcommerce.routinefinder.enquiry.graph.data

import com.e2x.bigcommerce.routinefinder.enquiry.Question
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder.*
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder.AGE_ID
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder.SKIN_TYPE_ID

class RoutineEnquiryAnswerer {
    public static Closure<Question> ONE_ANSWER = { String questionId, Object value -> new Question(questionId, [value]) }

    static Closure<Question> AGE_ANSWER = ONE_ANSWER.curry(AGE_ID)
    static Closure<Question> SKIN_TYPE_ANSWER = ONE_ANSWER.curry(SKIN_TYPE_ID)
    static Closure<Question> SKIN_CONCERNS_ANSWER = ONE_ANSWER.curry(SKIN_CONCERNS_ID)

    static void answer(Questionnaire questionnaire, String questionId, Object answer) {
        questionnaire
                .findQuestionBy(questionId)
                .get()
                .answer = answer
    }
}
