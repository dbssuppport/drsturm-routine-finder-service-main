package com.e2x.bigcommerce.routinefinder.cli.mindmup

import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphImportInterop
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*
import com.e2x.bigcommerce.routinefinder.cli.RoutineGraphVertexImporter
import spock.lang.Specification

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.codeFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.expressionFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.questionIdFor

class MindMupJsonCommandExecutorSpec extends Specification {

    MindMupJsonRoutineGraphConfigurationImporter testObj
    RoutineGraphRepository routineGraphRepository = Mock()

    void setup() {
        testObj = new MindMupJsonRoutineGraphConfigurationImporter(new RoutineGraphVertexFactory())
    }

    void 'can import a valid mindmup export'() {
        given:
        def mindmup = this.getClass().getResourceAsStream('/valid.json')
        def reader = new InputStreamReader(mindmup)

        and:
        def importedGraph = new InMemoryRoutineGraph()
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def vertexImporter = new RoutineGraphVertexImporter(testImporter.call(importedGraph), errorReporting, routineGraphRepository, true)

        when:
        testObj.read(reader, vertexImporter)

        then:
        importedGraph.findRoots().size() == 1

        and:
        def rootVertex = importedGraph.findRoots().first()
        rootVertex.vertexType == VertexType.QUESTION

        and:
        questionIdFor(rootVertex) == 'age'

        and:
        def questionVertices = importedGraph.findChildVerticesFor(rootVertex)
        def optionsNode = questionVertices.find { v -> VertexType.OPTIONS == v.vertexType }
        def conditionNodes = questionVertices.findAll({ v -> v.vertexType == VertexType.CONDITION })

        and:
        optionsNode != null
        conditionNodes.size() == 2

        and:
        importedGraph.findChildVerticesFor(optionsNode).collect({v -> codeFor(v) }).toSet() == ['18-24', '25-34'] as Set

        and:
        conditionNodes.collect({ c -> expressionFor(c)}).toSet() == ['age is 18-24', 'age is 25-34'] as Set
    }

    private Closure<RoutineGraphImportInterop> testImporter = { RoutineGraph testGraph ->
        new RoutineGraphImportInterop() {
            @Override
            RoutineGraph create(Vertex vertex) {
                testGraph.add(vertex)
                return testGraph
            }

            @Override
            void addVertex(RoutineGraph graph, Vertex vertex) {
                graph.addVertex(vertex)
            }

            @Override
            void link(RoutineGraph graph, Vertex vertex, long parentVertexId) {
                def parentVertex = graph.findById(parentVertexId)

                graph.link(parentVertex, vertex)
            }

            @Override
            void finalise(RoutineGraph graph) {

            }
        }
    }
}
