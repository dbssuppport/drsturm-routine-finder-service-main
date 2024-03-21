package com.e2x.bigcommerce.routinefinderservice.controller

import com.e2x.bigcommerce.routinefindermodel.ErrorCode
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@SpringBootTest
@ActiveProfiles('integration')
@AutoConfigureMockMvc
class RoutineFinderControllerValidationSpec extends Specification {
    @Autowired
    private MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    def "when a wrong answer is given an invalid answer error is returned "() {
        given:
        def INVALID_ANSWER = "wrong answer is not allowed as an answer. Please select an answer from one of the options below."

        def firstResponse = apiGetRoutineEnquiry()
        def firstRoutineEnquiry = toRoutineEnquiry(firstResponse)
        answerAgeQuestion(firstRoutineEnquiry, "wrong answer")

        when:
        def secondResponse = apiSubmitAnswers(toJson(firstRoutineEnquiry))
        def secondRoutineEnquiry = toRoutineEnquiry(secondResponse)

        then:
        secondRoutineEnquiry.questions.error
        secondRoutineEnquiry.questions[0].error.code == ErrorCode.INVALID_ANSWER
        secondRoutineEnquiry.questions[0].error.message == INVALID_ANSWER
        secondRoutineEnquiry.questions[0].answers[0] == "wrong answer"
        secondResponse.status == 400
    }

    def "when a different wrong answer is given the invalid answer error is modified"() {
        given:
        def INVALID_ANSWER = "different incorrect answer is not allowed as an answer. Please select an answer from one of the options below."

        def firstResponse = apiGetRoutineEnquiry()
        def firstRoutineEnquiry = toRoutineEnquiry(firstResponse)
        answerAgeQuestion(firstRoutineEnquiry, "different incorrect answer")

        when:
        def secondResponse = apiSubmitAnswers(toJson(firstRoutineEnquiry))
        def secondRoutineEnquiry = toRoutineEnquiry(secondResponse)

        then:
        secondRoutineEnquiry.questions.error
        secondRoutineEnquiry.questions[0].error.code == ErrorCode.INVALID_ANSWER
        secondRoutineEnquiry.questions[0].error.message == INVALID_ANSWER
        secondRoutineEnquiry.questions[0].answers[0] == "different incorrect answer"
        secondResponse.status == 400
    }

    def "when an answer is not answered, it is not read as invalid"() {
        given:
        def firstResponse = apiGetRoutineEnquiry()
        def firstRoutineEnquiry = toRoutineEnquiry(firstResponse)

        when:
        def secondResponse = apiSubmitAnswers(toJson(firstRoutineEnquiry))
        def secondRoutineEnquiry = toRoutineEnquiry(secondResponse)

        then:
        secondResponse.status == 200
        secondRoutineEnquiry.questions[1].error == null
    }

    def "when the wrong amount of answers are given an max answer error is thrown"() {
        given:
        def MAX_ANSWER = "Only 1 option can be selected as an answer. Please select 1 option from the options below."
        def firstAnswer = "dry"
        def secondAnswer = "oily"

        def firstResponse = apiGetRoutineEnquiry()
        def firstRoutineEnquiry = toRoutineEnquiry(firstResponse)
        firstRoutineEnquiry.questions[1].answers.add(firstAnswer)
        firstRoutineEnquiry.questions[1].answers.add(secondAnswer)

        when:
        def secondResponse = apiSubmitAnswers(toJson(firstRoutineEnquiry))
        def secondRoutineEnquiry = toRoutineEnquiry(secondResponse)

        then:
        secondResponse.status == 400
        secondRoutineEnquiry.questions[1].error
        secondRoutineEnquiry.questions[1].error.code == ErrorCode.MAX_ANSWER
        secondRoutineEnquiry.questions[1].error.message == MAX_ANSWER

        secondRoutineEnquiry.questions[1].answers.size() == 2
        secondRoutineEnquiry.questions[1].answers[0] == firstAnswer
        secondRoutineEnquiry.questions[1].answers[1] == secondAnswer
    }

    def "When answering a different question with the wrong amount of answers the error message will update to show the max amount of each question"() {
        given:
        def MAX_ANSWER = "Only 1 option can be selected as an answer. Please select 1 option from the options below."
        def firstResponse = apiGetRoutineEnquiry()
        def firstRoutineEnquiry = toRoutineEnquiry(firstResponse)
        firstRoutineEnquiry.questions[0].answers.add("18-24")
        firstRoutineEnquiry.questions[0].answers.add("25-34")

        when:
        def secondResponse = apiSubmitAnswers(toJson(firstRoutineEnquiry))
        def secondRoutineEnquiry = toRoutineEnquiry(secondResponse)

        then:
        secondResponse.status == 400
        secondRoutineEnquiry.questions[0].error
        secondRoutineEnquiry.questions[0].error.code == ErrorCode.MAX_ANSWER
        secondRoutineEnquiry.questions[0].error.message == MAX_ANSWER

    }

    def apiGetRoutineEnquiry() {
        mvc.perform(get("/bc/store/1/customer/1/routine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
    }

    def apiSubmitAnswers(request) {
        mvc.perform(put("/bc/store/1/customer/1/routine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andReturn().response
    }

    def answerAgeQuestion(routineEnquiry, age) {
        routineEnquiry.questions[0].answers.add(age)
    }

    RoutineEnquiry toRoutineEnquiry(response){
        objectMapper.readValue(response.getContentAsString(), RoutineEnquiry)
    }

    def toJson(request){
        objectMapper.writeValueAsString(request)
    }

}
