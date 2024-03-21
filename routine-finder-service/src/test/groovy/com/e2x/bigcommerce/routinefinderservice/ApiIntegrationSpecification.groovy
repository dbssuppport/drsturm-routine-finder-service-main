package com.e2x.bigcommerce.routinefinderservice

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.e2x.bigcommerce.routinefinderservice.configuration.QuestionIds
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.e2x.bigcommerce.routinefinderservice.TestUtils.answer

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class ApiIntegrationSpecification extends Specification implements ApiRoutineFinder {

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    MockMvc mockMvc

    RoutineEnquiry completedRoutineEnquiry() {
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()
        answer(routineEnquiry, 'age', '25-34')
        answer(routineEnquiry, QuestionIds.SKIN_TYPE, 'dry')
        answer(routineEnquiry, QuestionIds.SKIN_TONE, 'light')
        answer(routineEnquiry, QuestionIds.DAILY_USAGE, '4')

        RoutineEnquiry updatedRoutineEnquiry = advanceRoutineEnquiry(routineEnquiry)

        answer(updatedRoutineEnquiry, QuestionIds.SKIN_CONCERNS, 'pollution')
        updatedRoutineEnquiry = advanceRoutineEnquiry(updatedRoutineEnquiry)

        answer(updatedRoutineEnquiry, QuestionIds.ROUTINE_TYPE, 'basic')
        updatedRoutineEnquiry = advanceRoutineEnquiry(updatedRoutineEnquiry)

        assert updatedRoutineEnquiry.complete

        updatedRoutineEnquiry
    }
}
