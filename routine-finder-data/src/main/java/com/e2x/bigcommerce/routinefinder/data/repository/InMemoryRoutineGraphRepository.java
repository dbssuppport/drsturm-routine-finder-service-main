package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph;

public class InMemoryRoutineGraphRepository implements RoutineGraphRepository {

    private RoutineGraph routineGraph;

    public InMemoryRoutineGraphRepository() {
        this(new InMemoryRoutineGraph());
    }

    public InMemoryRoutineGraphRepository(RoutineGraph routineGraph) {
        this.routineGraph = routineGraph;
    }

    @Override
    public RoutineGraph fetchCurrent() {
        return routineGraph;
    }

    @Override
    public void save(RoutineGraph graph) {
        this.routineGraph = graph;
    }
}
