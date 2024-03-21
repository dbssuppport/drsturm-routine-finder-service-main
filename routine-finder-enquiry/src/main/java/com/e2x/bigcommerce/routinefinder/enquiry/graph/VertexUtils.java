package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;

import static com.e2x.bigcommerce.routinefinder.enquiry.TextUtils.replaceWhitespace;
import static com.e2x.bigcommerce.routinefinder.enquiry.TextUtils.trim;

public class VertexUtils {
    private static final String QUESTION_ID = "questionId";
    private static final String CODE = "code";
    private static final String EXPR = "expr";
    private static final String STEPS = "steps";
    private static final String PROGRESS = "progress";

    public static String stepsFor(Vertex vertex) {
        return getStringAttributeFor(vertex, STEPS).toLowerCase();
    }

    public static String expressionFor(Vertex vertex) {
        return getStringAttributeFor(vertex, EXPR);
    }

    public static String codeFor(Vertex vertex) {
        return getStringAttributeFor(vertex, CODE);
    }

    public static String questionIdFor(Vertex vertex) {
        return getStringAttributeFor(vertex, QUESTION_ID);
    }

    public static Integer progressFor(Vertex vertex) {
        return getIntegerAttributeFor(vertex, PROGRESS);
    }

    public static void setProgressFor(Vertex vertex, Integer progress) {
        vertex.addAttribute(PROGRESS, progress);
    }

    public static Vertex newRoutineFor(long id, String steps) {
        var vertex = new Vertex(id, VertexType.ROUTINE);
        vertex.addAttribute(STEPS, steps);
        return vertex;
    }

    public static Vertex newRoutineFor(String steps) {
        var vertex = new Vertex(VertexType.ROUTINE);
        vertex.addAttribute(STEPS, steps);
        return vertex;
    }

    public static Vertex newConditionFor(long id, String expression) {
        var vertex = new Vertex(id, VertexType.CONDITION);
        vertex.addAttribute(EXPR, replaceWhitespace(trim(expression.toLowerCase())));
        return vertex;
    }

    public static Vertex newConditionFor(String expression) {
        var vertex = new Vertex(VertexType.CONDITION);
        vertex.addAttribute(EXPR, expression.toLowerCase());
        return vertex;
    }

    public static Vertex newQuestionVertexFor(long id, String questionId) {
        var vertex = new Vertex(id, VertexType.QUESTION);
        vertex.addAttribute(QUESTION_ID, questionId.toLowerCase());
        return vertex;
    }

    public static Vertex newOptionValueVertex(long id, String code) {
        var vertex = new Vertex(id, VertexType.OPTION_VALUE);
        vertex.addAttribute(CODE, code.toLowerCase());
        return vertex;
    }

    public static Vertex newOptionValueVertex(String code) {
        var vertex = new Vertex(VertexType.OPTION_VALUE);
        vertex.addAttribute(CODE, code.toLowerCase());
        return vertex;
    }

    public static Vertex newOptionsVertex() {
        return new Vertex(VertexType.OPTIONS);
    }

    public static Vertex newOptionsVertex(long id) {
        return new Vertex(id, VertexType.OPTIONS);
    }

    public static Vertex newQuestionVertexFor(String questionId) {
        var vertex = new Vertex(VertexType.QUESTION);
        vertex.addAttribute(QUESTION_ID, questionId);
        return vertex;
    }


    public static String getRoutinePathToRootFor(Vertex vertex, RoutineGraph routineGraph) {
        var pathToRootIterator = new GraphPathToRootIterator<>(vertex, routineGraph);
        var pathAsString = new StringBuffer();

        if (pathToRootIterator.hasNext()) {
            var path = pathToRootIterator.next();
            path.getVertexList().forEach(v -> {
                switch (v.getVertexType()) {
                    case QUESTION:
                        pathAsString.insert(0, String.format("question: %s", questionIdFor(v)));
                        break;
                    case CONDITION:
                        pathAsString.insert(0, String.format("condition: %s", expressionFor(v)));
                        break;
                    default:
                        pathAsString.insert(0,  v);
                }
                pathAsString.insert(0, "->");
            });
        }

        return pathAsString.toString();
    }

    private static String getStringAttributeFor(Vertex vertex, String key) {
        return (String) vertex.getAttributes().get(key);
    }

    private static Integer getIntegerAttributeFor(Vertex vertex, String key) {
        if (vertex.getAttributes().containsKey(key)) {
            var value = vertex.getAttributes().get(key);
            if (value != null) {
                return Integer.valueOf(value.toString());
            }
        }

        return null;
    }
}
