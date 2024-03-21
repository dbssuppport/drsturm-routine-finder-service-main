package com.e2x.bigcommerce.routinefinder.data.repository

import com.e2x.bigcommerce.routinefinder.data.examples.TestGraph
import com.e2x.bigcommerce.routinefinder.data.repository.graph.GraphMLReaderWriter
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph
import spock.lang.Specification
import spock.lang.Stepwise

import java.nio.charset.StandardCharsets

@Stepwise
class GraphMLReaderWriterSpec extends Specification {
    GraphMLReaderWriter testObj

    void setup() {
        testObj = new GraphMLReaderWriter()
    }

    void 'can export graph to stream'() {
        given:
        StringWriter output = new StringWriter()

        and:
        def graph = TestGraph.createDemoRoutineGraph()

        when:
        testObj.write(output, graph)

        then:
        output.toString()

        and:
        noExceptionThrown()
    }

    void 'can import graph from stream'() {
        given:
        def reader = new FileReader(new File(ClassLoader.getSystemResource("routine-graph.gml").toURI()), StandardCharsets.UTF_8)

        when:
        def importedGraph = testObj.read(reader)

        then:
        importedGraph
    }

    void 'imported graph after a write is the same as the original graph'() {
        given:
        def originalGraph = TestGraph.createDemoRoutineGraph()

        and:
        StringWriter output = new StringWriter()
        testObj.write(output, originalGraph)

        and:
        def reader = new StringReader(output.toString())

        when:
        def loadedGraph = testObj.read(reader)

        then:
        graphsAreEqual(originalGraph, loadedGraph)
    }

    private static boolean graphsAreEqual(RoutineGraph original, RoutineGraph other) {
        assert original.vertexSet() == other.vertexSet()
        assert original.edgeSet().size() == other.edgeSet().size()

        original.edgeSet().each {edge ->
            assert other.containsEdge(original.getEdgeSource(edge), original.getEdgeTarget(edge))
        }

        return true
    }
}
