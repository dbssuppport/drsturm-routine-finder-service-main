package com.e2x.bigcommerce.routinefinder.enquiry.graph

import spock.lang.Specification

class VertexSpec extends Specification {

    void 'equality check'() {
        given:
        def aVertex = new Vertex(1)

        when:
        def vertex = new Vertex(1)

        then:
        aVertex == vertex

        when:
        def different = new Vertex(2)

        then:
        different != vertex
    }
}
