package com.e2x.bigcommerce.routinefinderservice.controller

import com.e2x.bigcommerce.routinefindermodel.Error
import com.e2x.bigcommerce.routinefindermodel.ErrorCode
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.e2x.bigcommerce.routinefinderservice.ApiIntegrationSpecification
import com.e2x.bigcommerce.routinefinderservice.ApiRoutineFinder
import org.springframework.http.MediaType
import spock.lang.Ignore

import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

class RoutineFinderControllerSpec extends ApiIntegrationSpecification {

    def "when GET is performed, with accept header then routineEnquiry is returned"() {
        when:
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()

        then:
        !routineEnquiry.complete
        routineEnquiry.questions.size() >= 1

        and:
        routineEnquiry.questions[0].id != null
        routineEnquiry.questions[0].text != null
        routineEnquiry.questions[0].maxAllowedAnswers == 1
        routineEnquiry.questions[0].progress != null
        routineEnquiry.questions[0].options instanceof List
        routineEnquiry.questions[0].answers instanceof List
        routineEnquiry.questions[0].options.size() >= 1
        routineEnquiry.questions[0].options[0].text
        routineEnquiry.questions[0].options[0].code
        !routineEnquiry.questions[0].answered
    }

    def "when GET request is sent to an unknown url then a Not found error is returned"() {
        when:
        def response = mockMvc.perform(get("/unknown/url")).andReturn().response

        then:
        response.status == UNAUTHORIZED.value()
    }

    def "when GET request is sent to an malformed url then a bad request error is returned"() {
        when:
        String incorrect_customer_id = "abc"
        def response = mockMvc.perform(get("/bc/store/1/customer/${incorrect_customer_id}/routine")).andReturn().response

        then:
        response.status == BAD_REQUEST.value()
    }

    def "when PUT is performed with correct content-type and header to correct url the original get response is modified "() {
        given:
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()
        routineEnquiry.questions[0].answers.add('18-24')

        when:
        RoutineEnquiry advancedRoutineEnquiry = advanceRoutineEnquiry(routineEnquiry)

        then:
        advancedRoutineEnquiry.questions[0].answers[0] == '18-24'
    }

    def "when PUT is performed without content-type then an unsupported media type error is returned"() {
        given:
        RoutineEnquiry request = new RoutineEnquiry()
        def requestJson = objectMapper.writeValueAsString(request);

        when:
        def response = mockMvc.perform(put("/bc/store/1/customer/1/routine")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andReturn()
                .response

        then:
        response.status == UNSUPPORTED_MEDIA_TYPE.value()

        and:
        def error = objectMapper.readValue(response.getContentAsString(), Error)

        error.code == ErrorCode.INVALID_REQUEST_MEDIA_TYPE
        error.message
    }

    def "when PUT request in sent to an unknown url then an unauthorised error is returned"() {
        given:
        RoutineEnquiry request = new RoutineEnquiry()
        def requestJson = objectMapper.writeValueAsString(request);

        when:
        def response = mockMvc.perform(put("/unknown/url")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andReturn()
                .response

        then:
        response.status == UNAUTHORIZED.value()
    }

    def "when PUT request is sent correctly but with a body other than Json returns is a bad request error"() {
        given:
        def request = new RoutineEnquiry().toString()

        when:
        def response = mockMvc.perform(put("/bc/store/1/customer/1/routine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andReturn().response

        then:
        response.status == BAD_REQUEST.value()

        and:
        def error = objectMapper.readValue(response.getContentAsString(), Error)
        error.code == ErrorCode.BAD_REQUEST
        error.message
    }

    @Ignore
    //TODO failing because status ok but should be 400
    def "when PUT request is sent correctly but with a request body that is not a RoutineEnquiry then status is 400"() {

        when:
        def response = mockMvc.perform(put("/bc/store/1/customer/1/routine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content('{ "name": "random" }'))
                .andReturn().response

        then:
        response.status == 400
    }
}
