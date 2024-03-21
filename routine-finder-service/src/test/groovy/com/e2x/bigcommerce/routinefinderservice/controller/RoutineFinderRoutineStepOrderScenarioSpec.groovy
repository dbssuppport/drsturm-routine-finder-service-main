package com.e2x.bigcommerce.routinefinderservice.controller

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.e2x.bigcommerce.routinefinderservice.ApiIntegrationSpecification

class RoutineFinderRoutineStepOrderScenarioSpec extends ApiIntegrationSpecification {

    void 'routine steps order must be respected'() {
        given:
        RoutineEnquiry routineEnquiry = completedRoutineEnquiry()

        when:
        RoutineEnquiry routineStoreA = advanceRoutineEnquiry(routineEnquiry, 'default')

        then:
        routineStoreA.routines[0].steps[0].sku.id == '10-200-05'
        routineStoreA.routines[0].steps[1].sku.id == '07-200-06'
        routineStoreA.routines[0].steps[2].sku.id == '07-300-18'
    }

}
