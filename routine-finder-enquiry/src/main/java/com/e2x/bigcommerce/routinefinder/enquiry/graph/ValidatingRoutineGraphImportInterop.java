package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphImportInterop;
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.validator.GraphConditionSiblingsValidator;
import lombok.extern.slf4j.Slf4j;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.getRoutinePathToRootFor;

@Slf4j
public class ValidatingRoutineGraphImportInterop implements RoutineGraphImportInterop {

    private final RoutineGraphErrorReporting routineGraphErrorReporting;
    private final VertexValidator vertexValidator;

    public ValidatingRoutineGraphImportInterop(RoutineGraphErrorReporting routineGraphErrorReporting, VertexValidator vertexValidator) {
        this.routineGraphErrorReporting = routineGraphErrorReporting;
        this.vertexValidator = vertexValidator;
    }

    @Override
    public RoutineGraph create(Vertex vertex) {
        var routineGraph = new InMemoryRoutineGraph();

        vertexValidator.validateVertex(vertex, routineGraph);

        routineGraph.addVertex(vertex);

        return routineGraph;
    }

    @Override
    public void addVertex(RoutineGraph graph, Vertex vertex) {
        vertexValidator.validateVertex(vertex, graph);

        graph.addVertex(vertex);
    }

    @Override
    public void link(RoutineGraph graph, Vertex vertex, long parentVertexId) {
        vertexValidator.validateLinkVertex(vertex, parentVertexId, graph);

        var parentVertex = graph.findById(parentVertexId);
        graph.link(parentVertex, vertex);
    }

    @Override
    public void finalise(RoutineGraph graph) {
        if (graph.findRoots().size() > 1) {
            routineGraphErrorReporting.errorOn(graph, "more than one root was found on routine graph.");
        }

        validateSiblings(graph);

        graph.vertexSet()
                .stream()
                .filter(v -> graph.outgoingEdgesOf(v).size() == 0)
                .filter(v -> v.getVertexType() != VertexType.OPTION_VALUE)
                .filter(v -> v.getVertexType() != VertexType.ROUTINE)
                .forEach(v -> routineGraphErrorReporting.errorOn(v, String.format("vertex of type %s cannot be a leaf node. Check path %s.", v.getVertexType(), getRoutinePathToRootFor(v, graph))));
    }

    private void validateSiblings(RoutineGraph routineGraph) {
        var validator = new GraphConditionSiblingsValidator(routineGraphErrorReporting);
        validator.validate(routineGraph);
    }

}
