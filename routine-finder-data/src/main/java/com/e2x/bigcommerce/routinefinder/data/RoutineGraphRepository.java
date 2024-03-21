package com.e2x.bigcommerce.routinefinder.data;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;

public interface RoutineGraphRepository {
    RoutineGraph fetchCurrent();
    void save(RoutineGraph graph);
}
