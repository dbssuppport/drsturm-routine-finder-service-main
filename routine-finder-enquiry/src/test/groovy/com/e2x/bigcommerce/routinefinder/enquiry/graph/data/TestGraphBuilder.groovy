package com.e2x.bigcommerce.routinefinder.enquiry.graph.data

import com.e2x.bigcommerce.routinefinder.enquiry.Option
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Edge
import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex

import static TestQuestionBuilder.AGE
import static TestQuestionBuilder.clone
import static TestRoutineBuilder.MEDIUM_ROUTINE
import static TestRoutineBuilder.SIMPLE_ROUTINE
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.OptionNodeBuilder.*
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.data.TestQuestionBuilder.SKIN_TYPE

class TestGraphBuilder {

    /**
     * Covered Scenario:
     * - question asked regardless of previous answer
     * - different routine returned based on question answer
     *
     * age -> skin type -> [age is 18-25] -> simple routine
     *                  -> [age is 26-35] -> medium routine
     */
    static RoutineGraph aRoutineGraph() {
        def routineGraph = new InMemoryRoutineGraph()

        def age = routineGraph.add(AGE)
        def skinType = routineGraph.add(SKIN_TYPE)
        def simpleRoutine = routineGraph.add(SIMPLE_ROUTINE)
        def mediumRoutine = routineGraph.add(MEDIUM_ROUTINE)

        routineGraph.addEdge(age, skinType)
        routineGraph.link(skinType, 'age is 18-25', simpleRoutine)
        routineGraph.link(skinType, 'age is 26-35', mediumRoutine)

        return routineGraph
    }

    /**
     * Covered Scenario
     * - question to list of options
     * - same question to different list of options
     *
     * age -> [age is 18-25]  -> skin type -> options[light, dark, very dark]
     *     -> [age is 26-35] -> skin type -> options[light, dark]
     * @return
     */
    static RoutineGraph questionWithOptionGraph() {
        def routineGraph = new InMemoryRoutineGraph()

        def age = routineGraph.add(AGE)
        def skinType = routineGraph.add(SKIN_TYPE)
        def otherSkinType = routineGraph.add(clone(SKIN_TYPE))

        addOptionsTo(routineGraph, skinType, [LIGHT, DARK, VERY_DARK])
        addOptionsTo(routineGraph, otherSkinType, [LIGHT, DARK])

        routineGraph.link(age, 'age is 18-25', skinType)
        routineGraph.link(age, 'age is 26-35', otherSkinType)

        return routineGraph
    }

    static void addOptionsTo(RoutineGraph graph, Vertex questionNode, List<Option> options) {
        def optionNode = OPTION_NODE.call()
        graph.addVertex(optionNode)

        options.each {
            def optionValueNode = OPTION_VALUE_NODE.call(it.code)
            graph.addVertex(optionValueNode)
            graph.addEdge(optionNode, optionValueNode, new Edge())
        }

        graph.addEdge(questionNode, optionNode, new Edge())
    }

}
