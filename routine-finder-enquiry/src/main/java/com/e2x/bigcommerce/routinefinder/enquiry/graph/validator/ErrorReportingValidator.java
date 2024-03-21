package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;

class ErrorReportingValidator<T extends Vertex> {
    private final RoutineGraphErrorReporting routineGraphErrorReporting;

    public ErrorReportingValidator(RoutineGraphErrorReporting routineGraphErrorReporting) {
        this.routineGraphErrorReporting = routineGraphErrorReporting;
    }

    void errorOn(T vertex, String errorMessage) {
        routineGraphErrorReporting.errorOn(vertex, errorMessage);
    }
}
