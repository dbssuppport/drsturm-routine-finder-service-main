package com.e2x.bigcommerce.routinefinder.enquiry.graph

import com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestGraphBuilder
import spock.lang.Specification

class RoutineGraphStatsHelperSpec extends Specification {

    void 'can calculate path lengths'() {
        given:
        def routineGraph = TestGraphBuilder.aRoutineGraph()

        when:
        def result = RoutineGraphStatsHelper.longestPathIn(routineGraph)

        then:
        result == 3
    }
}
