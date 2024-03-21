package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a routine graph instance for a customer.
 *
 * When traversed using a GraphIterator it will only return outgoing edges of a vertex if the vertex condition has been met.
 * This allows for the graph iterator to exit at the point where more information is required from the customer.
 */
@Slf4j
public class QuestionnaireAwareRoutineGraph extends DelegatedRoutineGraph {

    @Getter
    private final Questionnaire questionnaire;

    public QuestionnaireAwareRoutineGraph(Questionnaire questionnaire, RoutineGraph routineGraph) {
        super(routineGraph);

        this.questionnaire = questionnaire;
    }

    /**
     * only traverse outgoing edges if the vertex condition has been met.
     * @param vertex the vertex to find outgoing edges for
     * @return ordered set of edges
     */
    @Override
    public Set<Edge> outgoingEdgesOf(Vertex vertex) {
        if (log.isDebugEnabled()) {
            log.debug("finding outgoing edges of " + vertex.toString());
        }

        if (vertex.isTriggered(questionnaire)) {
            return super.outgoingEdgesOf(vertex).stream().sorted((o1, o2) -> {
                var o1Vertex = getEdgeTarget(o1);
                var o2Vertex = getEdgeTarget(o2);

                if (o1Vertex == null || o2Vertex == null) {
                    return 0;
                }

                return Integer.compare(o2Vertex.getVertexType().getPriority(), o1Vertex.getVertexType().getPriority());
            }).collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            return Sets.newLinkedHashSet();
        }
    }

}
