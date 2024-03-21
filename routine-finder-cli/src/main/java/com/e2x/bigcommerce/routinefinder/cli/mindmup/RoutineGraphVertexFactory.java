package com.e2x.bigcommerce.routinefinder.cli.mindmup;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineStepsUtils;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.*;

@Slf4j
@Component
public class RoutineGraphVertexFactory {
    private static final String OPTIONS = "options";
    private static final String STEP = "STEP ";
    private static final String[] CONDITION_KEY_WORDS = new String[] { "is", "is not", "not in", "in" };

    public Vertex createVertexFor(MindMupJsonNode node) {
        if (isConditionNode(node)) {
            return newConditionFor(node.getId(), node.getTitle());
        }

        if (isOptionsNode(node)) {
            return newOptionsVertex(node.getId());
        }

        if (isOptionValueNode(node)) {
            return newOptionValueVertex(node.getId(), node.getTitle());
        }

        if (isRoutineNode(node)) {
            String steps = RoutineStepsUtils.justifyStepsString(node.getTitle());
            return newRoutineFor(node.getId(), steps);
        }

        if (Strings.isNullOrEmpty(node.getTitle())) {
            log.warn(String.format("unknown node type found %s", node));
            return null;
        }

        return newQuestionVertexFor(node.getId(), node.getTitle());
    }

    private boolean isOptionsNode(MindMupJsonNode node) {
        return OPTIONS.equalsIgnoreCase(node.getTitle());
    }

    private boolean isRoutineNode(MindMupJsonNode node) {
        if (Strings.isNullOrEmpty(node.getTitle())) {
            return false;
        }

        return node
                .getTitle()
                .toUpperCase()
                .startsWith(STEP);
    }

    private boolean isConditionNode(MindMupJsonNode node) {
        return Arrays.stream(CONDITION_KEY_WORDS).anyMatch(k -> node.getTitle().contains(" " + k));
    }

    private boolean isOptionValueNode(MindMupJsonNode node) {
        if (node.getParentNode() == null) {
            return false;
        }

        return isOptionsNode(node.getParentNode());
    }

}
