package com.e2x.bigcommerce.routinefinderservice.controller

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.e2x.bigcommerce.routinefinderservice.ApiIntegrationSpecification

class RoutineFinderStoreScenarioSpec extends ApiIntegrationSpecification {

    void 'routine steps sku ids can be different between stores'() {
        given:
        RoutineEnquiry routineEnquiry = completedRoutineEnquiry()

        when:
        RoutineEnquiry routineStoreA = advanceRoutineEnquiry(routineEnquiry, 'default')
        RoutineEnquiry routineStoreB = advanceRoutineEnquiry(routineEnquiry, 'ww3msiylzo')

        then:
        routineStoreA.routines[0].steps[0].sku.id == '10-200-05'
        routineStoreB.routines[0].steps[0].sku.id == '10-200-04'
    }
}
