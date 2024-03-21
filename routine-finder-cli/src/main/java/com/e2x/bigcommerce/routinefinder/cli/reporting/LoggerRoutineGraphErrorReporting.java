package com.e2x.bigcommerce.routinefinder.cli.reporting;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerRoutineGraphErrorReporting implements RoutineGraphErrorReporting {

    private boolean graphInError = false;

    @Override
    public <T extends Vertex> void errorOn(T vertex, String errorMessage) {
        graphInError = true;
        log.error(String.format("error on vertex: %s message: %s", vertex.toString(), errorMessage));
    }

    @Override
    public void errorOn(RoutineGraph graph, String errorMessage) {
        graphInError = true;
        log.error(String.format("error %s reported on routine graph", errorMessage));
    }

    @Override
    public boolean hasErrors(RoutineGraph graph) {
        return graphInError;
    }
}
