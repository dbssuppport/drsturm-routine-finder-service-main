package com.e2x.bigcommerce.routinefinderservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles('integration')
class RoutineFinderServiceApplicationSpec extends Specification {

    @Autowired
    ApplicationContext context

    void 'context loads'() {
        expect:
        context
    }

}
