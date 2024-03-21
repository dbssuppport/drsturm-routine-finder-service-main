package com.e2x.bigcommerce.routinefinder.cli;

import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphImportInterop;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.RoutineProgressCalculator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class RoutineGraphVertexImporter implements VertexReaderListener {
    private final RoutineGraphImportInterop routineGraphImportInterop;
    private final RoutineGraphErrorReporting routineGraphErrorReporting;
    private final RoutineGraphRepository routineGraphRepository;
    private final boolean force;
    private RoutineGraph routineGraph;
    private final Map<Long, List<Vertex>> edgesToVertex;

    public RoutineGraphVertexImporter(RoutineGraphImportInterop routineGraphImportInterop,
            RoutineGraphErrorReporting routineGraphErrorReporting, RoutineGraphRepository routineGraphRepository,
            boolean force) {
        this.routineGraphImportInterop = routineGraphImportInterop;
        this.routineGraphErrorReporting = routineGraphErrorReporting;
        this.routineGraphRepository = routineGraphRepository;
        this.force = force;
        this.edgesToVertex = Maps.newHashMap();
    }

    @Override
    public void onVertexRead(Vertex vertex, long parentVertexId) {
        if (routineGraph == null) {
            routineGraph = routineGraphImportInterop.create(vertex);
        } else {
            routineGraphImportInterop.addVertex(routineGraph, vertex);
        }

        linkVertices(vertex, parentVertexId);
    }

    @Override
    public void onVertexReadFinished() {
        if (routineGraph != null) {
            routineGraphImportInterop.finalise(routineGraph);

            RoutineProgressCalculator routineProgressCalculator = new RoutineProgressCalculator();
            routineProgressCalculator.process(routineGraph);

            log.info("configuration parsed");
            log.info(String.format("routine counts: %s", routineGraph.vertexSet().stream()
                    .filter(v -> v.getVertexType().equals(VertexType.ROUTINE)).count()));

            if (force || !routineGraphErrorReporting.hasErrors(routineGraph)) {
                routineGraphRepository.save(routineGraph);
            } else {
                log.error("graph is not valid, and will not be published (use force if you wish to override).");
            }
        }
    }

    private void linkVertices(Vertex vertex, long parentVertexId) {
        if (parentVertexId != 0L) {
            linkOrStoreToParentVertices(vertex, parentVertexId);
        }

        if (edgesToVertex.containsKey(vertex.getId())) {
            linkToChildVertices(vertex);
        }
    }

    private void linkToChildVertices(Vertex vertex) {
        edgesToVertex.get(vertex.getId()).forEach(v -> {
            routineGraphImportInterop.link(routineGraph, v, vertex.getId());
        });

        edgesToVertex.remove(vertex.getId());
    }

    private void linkOrStoreToParentVertices(Vertex vertex, long parentVertexId) {
        var parentVertex = routineGraph.findById(parentVertexId);

        if (parentVertex != null) {
            routineGraph.link(parentVertex, vertex);
        } else {
            if (!edgesToVertex.containsKey(parentVertexId)) {
                edgesToVertex.put(parentVertexId, Lists.newArrayList());
            }

            edgesToVertex.get(parentVertexId).add(vertex);
        }
    }
}
