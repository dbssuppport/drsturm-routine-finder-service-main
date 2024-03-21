package com.e2x.bigcommerce.routinefinderservice.service.routine

import com.e2x.bigcommerce.routinefinder.enquiry.Question
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.e2x.bigcommerce.routinefinderservice.service.QuestionDefinitionService
import spock.lang.Specification

class QuestionMapperSpec extends Specification {

    QuestionMapper testObj

    QuestionDefinitionService questionDefinitionService

    void setup() {
        questionDefinitionService = Mock()

        testObj = new QuestionMapper(questionDefinitionService)
    }

    void 'it should return a new routine enquiry initialised with the questions returned by the routine enquiry service'() {
        given:
        def question = mockQuestion()

        and:
        def questionnaire = new Questionnaire()
        questionnaire.add(question)

        when:
        def result = testObj.mapFrom(null, questionnaire, new RoutineEnquiry())

        then:
        result
        result.size() == 1

        and:
        result[0].id == question.id
        result[0].maxAllowedAnswers == 1
        result[0].progress == 2
        result[0].options
        result[0].options.size() == 1
        result[0].options[0].code == "option-${question.id}"
        result[0].options[0].text == "option for ${question.id}"
        result[0].type == "text"
    }

    void 'it can map more than one question'() {
        given:
        def questionnaire = new Questionnaire()
        questionnaire.add(mockQuestion())
        questionnaire.add(mockQuestion())

        when:
        def result = testObj.mapFrom(null, questionnaire, new RoutineEnquiry())

        then:
        result
        result.size() == 2

        and:
        result*.id == questionnaire*.id
    }

    void 'it will reuse a question found on the routine enquiry instead of creating a new instance in order to keep external question state (like submitted answers)'() {
        given:
        def question = mockQuestion()
        def existingQuestion = new com.e2x.bigcommerce.routinefindermodel.Question(question.id, 'text', 1, 1, null, ['answer-1', 'answer-2'], null, null)

        and:
        def questionnaire = new Questionnaire()
        questionnaire.add(question)
        questionnaire.add(mockQuestion())

        when:
        def result = testObj.mapFrom(null, questionnaire, new RoutineEnquiry(questions: [existingQuestion]))

        then:
        result
        result.size() == 2

        and:
        result*.id == questionnaire*.id

        and:
        def mappedExistingQuestion = result.find {it.id == existingQuestion.id }
        mappedExistingQuestion.answers
        mappedExistingQuestion.answers == existingQuestion.answers
    }

    private Question mockQuestion(id = UUID.randomUUID().toString()) {
        def question = new Question(id)
        question.addOption("option-$id")
        question.progress = 2

        questionDefinitionService.getQuestionTextFor(null, question.id) >> "question $id text"
        questionDefinitionService.getMaxAllowedAnswerFor(null, question.id) >> 1
        questionDefinitionService.getOptionTextFor(null, question.id, question.options[0].code) >> "option for $id"
        questionDefinitionService.getQuestionTypeFor(null, question.id) >> "text"

        question
    }

}
