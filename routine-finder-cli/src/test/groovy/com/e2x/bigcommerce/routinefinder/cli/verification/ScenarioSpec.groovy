package com.e2x.bigcommerce.routinefinder.cli.verification

import spock.lang.Specification

class ScenarioSpec extends Specification {

    void 'should return list of allowed choices for question id'() {
        given:
        def scenario = new Scenario()

        and:
        def ageChoices = new AllowedChoices('age')
        ageChoices.addChoice('1')
        ageChoices.addChoice('2')

        scenario.addCriteria(ageChoices)

        when:
        def allowedChoices = scenario.getAllowedChoicesFor('age')

        then:
        !allowedChoices.isEmpty()

        and:
        allowedChoices.get() == ageChoices
    }
}
