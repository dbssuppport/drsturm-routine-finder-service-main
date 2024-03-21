package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.*;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.SkuNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.stepsFor;

class RoutineNodeValidator extends ErrorReportingValidator<Vertex> implements VertexValidator {

    private static final List<VertexType> ALLOWED_PARENTS = Lists.newArrayList(VertexType.CONDITION, VertexType.QUESTION);
    private final SkuNodeRepository skuNodeRepository;

    public RoutineNodeValidator(RoutineGraphErrorReporting routineGraphErrorReporting, SkuNodeRepository skuNodeRepository) {
        super(routineGraphErrorReporting);
        this.skuNodeRepository = skuNodeRepository;
    }

    @Override
    public void validateVertex(Vertex vertex, RoutineGraph routineGraph) {
        if (Strings.isNullOrEmpty(stepsFor(vertex))) {
            errorOn(vertex, "Missing steps on vertex");
        } else {
            Set<Step> skus = RoutineStepsUtils.toSteps(stepsFor(vertex));
            validateRoutineSteps(vertex, skus);
        }
    }

    public void validateRoutineSteps(Vertex vertex, Set<Step> skus) {
        for (Step step: skus) {
            if (skuNodeRepository.findNodeBy(step.getSkuId()).isEmpty()) {
                errorOn(vertex, String.format("sku %s does not exist", step.getSkuId()));
            }
        }
    }

    @Override
    public void validateLinkVertex(Vertex vertex, long parentVertexId, RoutineGraph graph) {
        var parentVertex = graph.findById(parentVertexId);

        if (!ALLOWED_PARENTS.contains(parentVertex.getVertexType())) {
            errorOn(vertex, String.format("Routine %s is not allowed to be a child of %s", vertex.getId(), parentVertex.getVertexType()));
        }
    }

}
