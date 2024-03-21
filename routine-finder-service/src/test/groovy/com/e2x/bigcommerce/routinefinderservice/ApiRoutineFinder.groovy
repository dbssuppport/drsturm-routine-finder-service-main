package com.e2x.bigcommerce.routinefinderservice

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

trait ApiRoutineFinder extends BaseApi {

    def <T> T startRoutineEnquiry(String storeHashId = '1', String customerId = '1', HttpStatus expectedStatus = HttpStatus.OK, Class<T> expectedClass = RoutineEnquiry) {

        def request = get("/bc/store/${storeHashId}/customer/${customerId}/routine")
                .accept(MediaType.APPLICATION_JSON)

        def result = mockMvc.perform(request)
                .andExpect(status().is(expectedStatus.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()

        objectMapper.readValue(result.getResponse().getContentAsString(), expectedClass)
    }

    def <T> T advanceRoutineEnquiry(RoutineEnquiry routineEnquiry, String storeHashId = '1', String customerId = '1', HttpStatus expectedStatus = HttpStatus.OK, Class<T> expectedClass = RoutineEnquiry) {

        def request = put("/bc/store/${storeHashId}/customer/${customerId}/routine")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(routineEnquiry))

        def result = mockMvc.perform(request)
                .andExpect(status().is(expectedStatus.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()

        objectMapper.readValue(result.getResponse().getContentAsString(), expectedClass)
    }

}