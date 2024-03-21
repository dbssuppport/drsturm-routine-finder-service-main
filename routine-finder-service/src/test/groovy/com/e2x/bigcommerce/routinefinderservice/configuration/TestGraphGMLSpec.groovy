package com.e2x.bigcommerce.routinefinderservice.configuration

import com.e2x.bigcommerce.routinefinder.data.configuration.RoutineFinderDataRepositoryConfiguration
import org.springframework.core.io.ClassPathResource
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class TestGraphGMLSpec extends Specification {

    void 'can load in-memory graph from resource'() {
        given:
        def inputStream = Objects.requireNonNull(new ClassPathResource('test-graph.gml').inputStream)
        def graphReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)

        and:
        def graphRepository = RoutineFinderDataRepositoryConfiguration.testGraphRoutineRepository(graphReader)

        when:
        def graph = graphRepository.fetchCurrent()

        then:
        graph
    }
}
