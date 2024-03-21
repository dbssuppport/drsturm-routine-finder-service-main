package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.questionIdFor;

@Slf4j
class QuestionNodeValidator extends ErrorReportingValidator<Vertex> implements VertexValidator {

    private static final List<VertexType> ALLOWED_TYPES = Lists.newArrayList(VertexType.CONDITION, VertexType.QUESTION);
    private final QuestionNodeRepository questionNodeRepository;

    public QuestionNodeValidator(RoutineGraphErrorReporting routineGraphErrorReporting, QuestionNodeRepository questionNodeRepository) {
        super(routineGraphErrorReporting);
        this.questionNodeRepository = questionNodeRepository;
    }

    @Override
    public void validateVertex(Vertex vertex, RoutineGraph routineGraph) {
        if (questionNodeRepository.findNodeBy(questionIdFor(vertex)).isEmpty()) {
            errorOn(vertex, String.format("Question with id %s is not in the allowed list.", questionIdFor(vertex)));
        }
    }

    @Override
    public void validateLinkVertex(Vertex vertex, long parentVertexId, RoutineGraph graph) {
        var parentVertex = graph.findById(parentVertexId);

        if (cannotBeAttachedTo(parentVertex)) {
            errorOn(vertex, String.format("Question vertex %s cannot be attached to type %s", questionIdFor(vertex), parentVertex.getClass().getName()));
        } else if (duplicatedOnPathToRoot(vertex, parentVertex, graph)) {
            errorOn(vertex, String.format("Question vertex %s cannot found twice in a path to a routine", questionIdFor(vertex)));
        }
    }

    private boolean duplicatedOnPathToRoot(Vertex vertex, Vertex parentVertex, RoutineGraph graph) {
        var rootPathIterator = new GraphPathToRootVertexIterator<>(parentVertex, graph);

        while (rootPathIterator.hasNext()) {
            var pathVertex = rootPathIterator.next();
            if (VertexType.QUESTION.equals(pathVertex.getVertexType()) && questionIdFor(vertex).equals(questionIdFor(pathVertex))) {
                log.info("found " + pathVertex.toString());
                return true;
            }
        }

        return false;
    }

    private boolean cannotBeAttachedTo(Vertex parentVertex) {
        return !ALLOWED_TYPES.contains(parentVertex.getVertexType());
    }
}
