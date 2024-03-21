package com.e2x.bigcommerce.routinefinder.enquiry.graph

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph
import spock.lang.Specification

class RoutineProgressCalculatorSpec extends Specification {

    void 'question length calculated as expected on a simple graph'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()

        and:
        def firstQuestion = addQuestionVertex(routineGraph)
        def secondQuestion = addQuestionVertex(routineGraph, firstQuestion)
        def thirdQuestion = addQuestionVertex(routineGraph, secondQuestion)

        and:
        addRoutine(routineGraph, thirdQuestion)

        and:
        def calculator = new RoutineProgressCalculator()

        when:
        calculator.process(routineGraph)

        then:
        VertexUtils.progressFor(firstQuestion) == 3
        VertexUtils.progressFor(secondQuestion) == 2
        VertexUtils.progressFor(thirdQuestion) == 1
    }

    void 'question progress will use the longest path to the same routine'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()

        and:
        def firstQuestion = addQuestionVertex(routineGraph)
        def secondQuestion = addQuestionVertex(routineGraph, firstQuestion)
        def lastQuestion = addQuestionVertex(routineGraph, secondQuestion)

        and:
        def routine = addRoutine(routineGraph, lastQuestion)

        and:
        def subPathQuestion1 = addQuestionVertex(routineGraph, firstQuestion)
        def subPathQuestion2 = addQuestionVertex(routineGraph, subPathQuestion1)
        def subPathQuestion3 = addQuestionVertex(routineGraph, subPathQuestion2)
        def subPathQuestion4 = addQuestionVertex(routineGraph, subPathQuestion3)
        routineGraph.link(subPathQuestion4, routine)

        and:
        def calculator = new RoutineProgressCalculator()

        when:
        calculator.process(routineGraph)

        then:
        VertexUtils.progressFor(firstQuestion) == 5
        VertexUtils.progressFor(secondQuestion) == 2
        VertexUtils.progressFor(lastQuestion) == 1

        and:
        VertexUtils.progressFor(subPathQuestion1) == 4
        VertexUtils.progressFor(subPathQuestion2) == 3
        VertexUtils.progressFor(subPathQuestion3) == 2
        VertexUtils.progressFor(subPathQuestion4) == 1
    }

    void 'question progress will use the longest path to any routine'() {
        given:
        def routineGraph = new InMemoryRoutineGraph()

        and:
        def firstQuestion = addQuestionVertex(routineGraph)
        def secondQuestion = addQuestionVertex(routineGraph, firstQuestion)
        def lastQuestion = addQuestionVertex(routineGraph, secondQuestion)

        and:
        addRoutine(routineGraph, lastQuestion)

        and:
        def subPathQuestion1 = addQuestionVertex(routineGraph, firstQuestion)
        addRoutine(routineGraph, subPathQuestion1)

        and:
        def calculator = new RoutineProgressCalculator()

        when:
        calculator.process(routineGraph)

        then:
        VertexUtils.progressFor(firstQuestion) == 3
        VertexUtils.progressFor(secondQuestion) == 2
        VertexUtils.progressFor(lastQuestion) == 1

        and:
        VertexUtils.progressFor(subPathQuestion1) == 1
    }

    private static Vertex addRoutine(RoutineGraph graph, Vertex parent = null) {
        addVertex(graph, parent) {
            new Vertex(VertexType.ROUTINE)
        }
    }

    private static Vertex addQuestionVertex(RoutineGraph graph, Vertex parent = null) {
        addVertex(graph, parent) {
            new Vertex(VertexType.QUESTION)
        }
    }

    private static Vertex addVertex(RoutineGraph graph, Vertex parentVertex, Closure<Vertex> vertexSupplier) {
        def newVertex = vertexSupplier()
        graph.add(newVertex)

        if (parentVertex) {
            graph.link(parentVertex, newVertex)
        }

        newVertex
    }
}
