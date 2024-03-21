package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*;

import java.util.Locale;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.codeFor;

class OptionValueNodeValidator extends ErrorReportingValidator<Vertex> implements VertexValidator {
    private static final VertexType ALLOWED_PARENT = VertexType.OPTIONS;
    private final OptionValueNodeRepository optionValueNodeRepository;

    public OptionValueNodeValidator(RoutineGraphErrorReporting routineGraphErrorReporting, OptionValueNodeRepository optionValueNodeRepository) {
        super(routineGraphErrorReporting);
        this.optionValueNodeRepository = optionValueNodeRepository;
    }

    @Override
    public void validateVertex(Vertex vertex, RoutineGraph routineGraph) {
        String optionCode = codeFor(vertex).toLowerCase(Locale.ROOT);
        if (optionValueNodeRepository.findNodeBy(optionCode).isEmpty()) {
            errorOn(vertex, String.format("could not find option definition for [%s].", codeFor(vertex)));
        }
    }

    @Override
    public void validateLinkVertex(Vertex vertex, long parentVertexId, RoutineGraph graph) {
        var parentVertex = graph.findById(parentVertexId);

        if (!ALLOWED_PARENT.equals(parentVertex.getVertexType())) {
            errorOn(vertex, String.format("option value %s can only be associated with an Options node.", codeFor(vertex)));
        }

    }
}
