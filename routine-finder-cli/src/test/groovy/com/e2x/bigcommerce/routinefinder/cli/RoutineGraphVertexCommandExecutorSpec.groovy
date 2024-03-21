package com.e2x.bigcommerce.routinefinder.cli

import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphImportInterop
import com.e2x.bigcommerce.routinefinder.enquiry.graph.DelegatingRoutineGraphImportInterop
import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex
import spock.lang.Specification

class RoutineGraphVertexCommandExecutorSpec extends Specification {

    RoutineGraphRepository routineGraphRepository = Mock()

    void 'adds vertex to routine graph'() {
        given:
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def importedGraph = new InMemoryRoutineGraph()
        def vertexImporter = new RoutineGraphVertexImporter(new DelegatingRoutineGraphImportInterop(importedGraph), errorReporting, routineGraphRepository, true)

        and:
        def expectedVertex = new Vertex()

        when:
        vertexImporter.onVertexRead(expectedVertex, 0L)

        then:
        importedGraph.containsVertex(expectedVertex)
    }

    void 'links vertex to parent vertex'() {
        given:
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def importedGraph = new InMemoryRoutineGraph()
        def vertexImporter = new RoutineGraphVertexImporter(new DelegatingRoutineGraphImportInterop(importedGraph), errorReporting, routineGraphRepository, true)

        and:
        def vertex = new Vertex()
        def parentVertex = new Vertex()

        and:
        vertexImporter.onVertexRead(parentVertex, 0L)

        when:
        vertexImporter.onVertexRead(vertex, parentVertex.getId())

        then:
        importedGraph.containsVertex(vertex)

        and:
        importedGraph.incomingEdgesOf(vertex).size() == 1
        importedGraph.incomingEdgesOf(vertex).collect({ importedGraph.getEdgeSource(it) }).size() == 1
        importedGraph.incomingEdgesOf(vertex).collect({ importedGraph.getEdgeSource(it) }).first() == parentVertex
    }

    void 'can delay vertex to parent link until parent vertex has been processed'() {
        given:
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def importedGraph = new InMemoryRoutineGraph()
        def vertexImporter = new RoutineGraphVertexImporter(new DelegatingRoutineGraphImportInterop(importedGraph), errorReporting, routineGraphRepository, true)

        and:
        def vertex = new Vertex()
        def parentVertex = new Vertex()

        and:
        vertexImporter.onVertexRead(vertex, parentVertex.getId())

        when:
        vertexImporter.onVertexRead(parentVertex, 0L)

        then:
        importedGraph.incomingEdgesOf(vertex).size() == 1
        importedGraph.incomingEdgesOf(vertex).collect({ importedGraph.getEdgeSource(it) }).size() == 1
        importedGraph.incomingEdgesOf(vertex).collect({ importedGraph.getEdgeSource(it) }).first() == parentVertex
    }

    void 'can link more than one child vertex to parent'() {
        given:
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def importedGraph = new InMemoryRoutineGraph()
        def vertexImporter = new RoutineGraphVertexImporter(new DelegatingRoutineGraphImportInterop(importedGraph), errorReporting, routineGraphRepository, true)

        and:
        def vertex1 = new Vertex()
        def vertex2 = new Vertex()
        def parentVertex = new Vertex()

        and:
        vertexImporter.onVertexRead(vertex1, parentVertex.getId())
        vertexImporter.onVertexRead(vertex2, parentVertex.getId())

        when:
        vertexImporter.onVertexRead(parentVertex, 0L)

        then:
        importedGraph.outgoingEdgesOf(parentVertex).size() == 2
        importedGraph.outgoingEdgesOf(parentVertex).collect({ importedGraph.getEdgeTarget(it) }) == [vertex1, vertex2]
    }

    void 'can link to more than one child regardless of the order of vertex being read'() {
        given:
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def importedGraph = new InMemoryRoutineGraph()
        def vertexImporter = new RoutineGraphVertexImporter(new DelegatingRoutineGraphImportInterop(importedGraph), errorReporting, routineGraphRepository, true)

        and:
        def vertex1 = new Vertex()
        def vertex2 = new Vertex()
        def parentVertex = new Vertex()

        and:
        vertexImporter.onVertexRead(vertex1, parentVertex.getId())
        vertexImporter.onVertexRead(parentVertex, 0L)

        when:
        vertexImporter.onVertexRead(vertex2, parentVertex.getId())

        then:
        importedGraph.outgoingEdgesOf(parentVertex).size() == 2
        importedGraph.outgoingEdgesOf(parentVertex).collect({ importedGraph.getEdgeTarget(it) }) == [vertex1, vertex2]
    }

    void 'on read finished it should finalise the routine graph'() {
        given:
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def graphInterop = Mock(RoutineGraphImportInterop)
        def routineGraph = new InMemoryRoutineGraph()
        graphInterop.create(_) >> routineGraph

        and:
        def vertexImporter = new RoutineGraphVertexImporter(graphInterop, errorReporting, routineGraphRepository, true)
        vertexImporter.onVertexRead(Mock(Vertex), 0L)

        when:
        vertexImporter.onVertexReadFinished()

        then:
        1 * graphInterop.finalise(routineGraph)
    }

    void 'does not error if no routine graph initialised on finished'() {
        given:
        def errorReporting = Mock(RoutineGraphErrorReporting)
        def graphInterop = Mock(RoutineGraphImportInterop)

        and:
        def vertexImporter = new RoutineGraphVertexImporter(graphInterop, errorReporting, routineGraphRepository, true)

        when:
        vertexImporter.onVertexReadFinished()

        then:
        0 * graphInterop.finalise(_)
    }
}
