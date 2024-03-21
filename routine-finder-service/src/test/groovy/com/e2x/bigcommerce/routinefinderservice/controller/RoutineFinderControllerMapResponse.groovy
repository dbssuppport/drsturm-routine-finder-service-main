package com.e2x.bigcommerce.routinefinderservice.controller

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@SpringBootTest
@ActiveProfiles('integration')
@AutoConfigureMockMvc
class RoutineFinderControllerMapResponse extends Specification {
    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    @Ignore
    def "when a routine is returned the skus in the steps are in the right order"(){
        def firstResponse = apiGetRoutineEnquiry()
        def firstRoutineEnquiry = toRoutineEnquiry(firstResponse)
        answerFirstQuestionSet(firstRoutineEnquiry, "18-24")

        when:
        def secondResponse = apiSubmitAnswers(toJson(firstRoutineEnquiry))
        def secondRoutineEnquiry = toRoutineEnquiry(secondResponse)

        then:
        secondRoutineEnquiry.complete
        secondRoutineEnquiry.routines[0].steps[0].sku.id == "body cream"
        secondRoutineEnquiry.routines[0].steps[1].sku.id == "cleanser"
        secondRoutineEnquiry.routines[0].steps[2].sku.id == "hyaluronic serum"

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

    def answerFirstQuestionSet(routineEnquiry, age) {
        routineEnquiry.questions[0].answers.add(age)
        routineEnquiry.questions[1].answers.add('dry')
        routineEnquiry.questions[2].answers.add('light')
        routineEnquiry.questions[3].answers.add('4')
    }

    def toRoutineEnquiry(response){
        objectMapper.readValue(response.getContentAsString(), RoutineEnquiry)
    }

    def toJson(request){
        objectMapper.writeValueAsString(request)
    }

}
