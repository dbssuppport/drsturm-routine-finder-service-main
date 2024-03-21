package com.e2x.bigcommerce.routinefinderservice

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MockMvc

trait BaseApi {
    abstract MockMvc getMockMvc()
    abstract ObjectMapper getObjectMapper()
}