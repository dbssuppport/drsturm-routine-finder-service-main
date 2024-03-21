package com.e2x.bigcommerce.routinefinder.data.examples;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*;

import static com.e2x.bigcommerce.routinefinder.data.examples.QuestionIds.*;
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.*;

public class TestGraph {

    public static RoutineGraph createDemoRoutineGraph() {
        RoutineGraph graph = new InMemoryRoutineGraph();

        var age = graph.add(createQuestionFor(AGE));
        var skinType = graph.add(createQuestionFor(SKIN_TYPE));
        var skinTone = graph.add(createQuestionFor(SKIN_TONE));
        var skinConcernsAge2534 = graph.add(createQuestionFor(SKIN_CONCERNS));
        var skinConcernsAge3544 = graph.add(createQuestionFor(SKIN_CONCERNS));
        var dailyCount = graph.add(createQuestionFor(DAILY_USAGE));
        var routineType2 = graph.add(createQuestionFor(ROUTINE_TYPE));
        var routineType3 = graph.add(createQuestionFor(ROUTINE_TYPE));
        var routineType4 = graph.add(createQuestionFor(ROUTINE_TYPE));
        var routineType5 = graph.add(createQuestionFor(ROUTINE_TYPE));

        addAgeOptions(age, graph);
        addSkinTypeOptions(skinType, graph);
        addSkinToneOptions(skinTone, graph);
        addSkinConcernsAge24_34Options(skinConcernsAge2534, graph);
        addSkinConcernsAgeOtherOptions(skinConcernsAge3544, graph);
        addDailyCountOptions(dailyCount, graph);
        addRoutineTypeOptions(routineType2, graph);
        addRoutineTypeOptions(routineType3, graph);
        addRoutineTypeOptions(routineType4, graph);
        addRoutineTypeOptions(routineType5, graph);

        var basicRoutine1 = graph.add(createRoutineFor("body cream"));

        var basicRoutine2 = graph.add(createRoutineFor("primer"));
        var enhancedRoutine2 = graph.add(createRoutineFor("body brush"));

        var basicRoutine3 = graph.add(createRoutineFor("anti pollution drops"));
        var enhancedRoutine3 = graph.add(createRoutineFor("anti-pollution-food"));

        var basicRoutine4 = graph.add(createRoutineFor("toner"));
        var enhancedRoutine4 = graph.add(createRoutineFor("mask"));


        var basicRoutine5 = graph.add(createRoutineFor("brightening lotion"));
        var enhancedRoutine5 = graph.add(createRoutineFor("calming"));

        graph.link(age, skinType);
        graph.link(skinType, skinTone);
        graph.link(skinTone, dailyCount);

        var conditionAgeIs2534 = createConditionFor(createExpressionFor(AGE, "is 25-34"));
        var conditionAgeIs3544 = createConditionFor(createExpressionFor(AGE, "is 35-44"));
        var conditionAgeOther = createConditionFor(createExpressionFor(AGE, "not in (25-34, 35-44)"));

        graph.link(dailyCount, conditionAgeOther);
        graph.link(dailyCount, conditionAgeIs2534);
        graph.link(dailyCount, conditionAgeIs3544);

        var conditionSkinConcernsInPollution = createConditionFor(createExpressionFor(SKIN_CONCERNS, "in (pollution)"));
        var conditionSkinConcernsNotInPollution = createConditionFor(createExpressionFor(SKIN_CONCERNS, "not in (pollution)"));

        var conditionSkinConcernsInWrinklesOrPollution = createConditionFor(createExpressionFor(SKIN_CONCERNS, "in (wrinkles, pollution)"));
        var conditionSkinConcernsInSun = createConditionFor(createExpressionFor(SKIN_CONCERNS, "in (sun)"));

        graph.link(conditionAgeOther, basicRoutine1);

        graph.link(conditionAgeIs2534, skinConcernsAge2534);
        graph.link(skinConcernsAge2534, conditionSkinConcernsInPollution);
        graph.link(skinConcernsAge2534, conditionSkinConcernsNotInPollution);
        graph.link(conditionSkinConcernsInPollution, routineType2);

        graph.link(routineType2, createExpressionFor(ROUTINE_TYPE, "is basic"), basicRoutine2);
        graph.link(routineType2, createExpressionFor(ROUTINE_TYPE, "is enhanced"), enhancedRoutine2);
        graph.link(conditionSkinConcernsNotInPollution, routineType3);
        graph.link(routineType3, createExpressionFor(ROUTINE_TYPE, "is basic"), basicRoutine3);
        graph.link(routineType3, createExpressionFor(ROUTINE_TYPE, "is enhanced"), enhancedRoutine3);

        graph.link(conditionAgeIs3544, skinConcernsAge3544);
        graph.link(skinConcernsAge3544, conditionSkinConcernsInWrinklesOrPollution);
        graph.link(skinConcernsAge3544, conditionSkinConcernsInSun);
        graph.link(conditionSkinConcernsInWrinklesOrPollution, routineType4);
        graph.link(conditionSkinConcernsInSun, routineType5);
        graph.link(routineType4, createExpressionFor(ROUTINE_TYPE, "is basic"), basicRoutine4);
        graph.link(routineType4, createExpressionFor(ROUTINE_TYPE, "is enhanced"), enhancedRoutine4);
        graph.link(routineType5, createExpressionFor(ROUTINE_TYPE, "is basic"), basicRoutine5);
        graph.link(routineType5, createExpressionFor(ROUTINE_TYPE, "is enhanced"), enhancedRoutine5);

        // initialise question progress
        RoutineProgressCalculator routineProgressCalculator = new RoutineProgressCalculator();
        routineProgressCalculator.process(graph);

        return graph;
    }

    private static void addRoutineTypeOptions(Vertex routineType, RoutineGraph graph) {
        var options = graph.add(createOptions());
        graph.link(routineType, options);
        graph.link(options, createOption("basic"));
        graph.link(options, createOption("enhanced"));
    }

    private static void addDailyCountOptions(Vertex dailyCount, RoutineGraph graph) {
        var options = graph.add(createOptions());
        graph.link(dailyCount, options);
        graph.link(options, createOption("4"));
        graph.link(options, createOption("7+"));
    }

    private static void addSkinConcernsAge24_34Options(Vertex skinConcerns, RoutineGraph graph) {
        var options = graph.add(createOptions());
        graph.link(skinConcerns, options);
        graph.link(options, createOption("pollution"));
        graph.link(options, createOption("sun"));
    }

    private static void addSkinConcernsAgeOtherOptions(Vertex skinConcerns, RoutineGraph graph) {
        var options = graph.add(createOptions());
        graph.link(skinConcerns, options);
        graph.link(options, createOption("pollution"));
        graph.link(options, createOption("sun"));
        graph.link(options, createOption("wrinkles"));
    }

    private static void addSkinToneOptions(Vertex skinTone, RoutineGraph graph) {
        var options = graph.add(createOptions());
        graph.link(skinTone, options);
        graph.link(options, createOption("fair"));
        graph.link(options, createOption("light"));
        graph.link(options, createOption("medium"));
        graph.link(options, createOption("olive"));
        graph.link(options, createOption("beige"));
        graph.link(options, createOption("dark"));
        graph.link(options, createOption("very dark"));
    }

    private static Vertex createRoutineFor(String stepName) {
        var steps = String.format("step 1: %s step 2: Cleanser step 3: Hyaluronic Serum", stepName);
        return newRoutineFor(steps);
    }

    private static Vertex createQuestionFor(String questionId) {
        return newQuestionVertexFor(questionId);
    }

    private static Vertex createConditionFor(String expression) {
        return newConditionFor(expression);
    }

    private static String createExpressionFor(String questionId, String expression) {
        return String.format("%s %s", questionId, expression);
    }

    private static void addAgeOptions(Vertex age, RoutineGraph graph) {
        var options = graph.add(createOptions());
        graph.link(age, options);
        graph.link(options, createOption("18-24"));
        graph.link(options, createOption("25-34"));
        graph.link(options, createOption("35-44"));
        graph.link(options, createOption("45-54"));
        graph.link(options, createOption("55-64"));
        graph.link(options, createOption("65+"));
    }

    private static void addSkinTypeOptions(Vertex node, RoutineGraph graph) {
        var options = graph.add(createOptions());
        graph.link(node, options);

        graph.link(options, createOption("dry"));
        graph.link(options, createOption("oily"));
        graph.link(options, createOption("combination"));
        graph.link(options, createOption("normal"));
    }

    private static Vertex createOption(String code) {
        return newOptionValueVertex(code);
    }

    private static Vertex createOptions() {
        return newOptionsVertex();
    }
}
