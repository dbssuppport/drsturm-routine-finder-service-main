package com.e2x.bigcommerce.routinefinder.enquiry.graph

import spock.lang.Specification

class RoutineGraphSpec extends Specification {

    void 'should find vertex by id'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        def vertex = new Vertex()
        graph.add(vertex)
        graph.add(new Vertex())

        expect:
        graph.findById(vertex.id) == vertex

        and:
        graph.findById(new Random().nextLong()) == null
    }

    void 'should find all roots to graph'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        def rootVertex1 = new Vertex()
        def rootVertex2 = new Vertex()
        graph.add(rootVertex1)
        graph.add(rootVertex2)
        graph.link(rootVertex1, new Vertex())

        expect:
        graph.findRoots() == [rootVertex1, rootVertex2] as Set
    }

    void 'can give you all children vertices to a vertex'() {
        given:
        def graph = new InMemoryRoutineGraph()

        and:
        def rootVertex1 = new Vertex()
        def childVertex1 = new Vertex()
        def childVertex2 = new Vertex()
        graph.add(rootVertex1)
        graph.link(rootVertex1, childVertex1)
        graph.link(rootVertex1, childVertex2)
        graph.link(childVertex1, new Vertex())

        expect:
        graph.findChildVerticesFor(rootVertex1) == [childVertex1, childVertex2] as Set

        and:
        graph.findChildVerticesFor(childVertex2) == [] as Set

        and:
        graph.findChildVerticesFor(childVertex1).size() == 1
    }
}
