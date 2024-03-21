package com.e2x.bigcommerce.routinefinder.enquiry

import spock.lang.Specification

class RoutineStepsUtilsSpec extends Specification {

    void 'it should trim and correct-ify steps strings'() {
        given:
        def stepsAsString = 'step 1: dst cleanser step 2: dst enzyme step 3: dst hyaluronic step 4: better b step 5: eye step 6: dst cream step 7: anti pollution drops step 8: sun drops step 9: spot treat untinted step 10: skin anti pigmentation.'

        when:
        def steps = RoutineStepsUtils.justifyStepsString(stepsAsString)

        then:
        steps == "step 1: dst-cleanser step 2: dst-enzyme step 3: dst-hyaluronic step 4: better-b step 5: eye step 6: dst-cream step 7: anti-pollution-drops step 8: sun-drops step 9: spot-treat-untinted step 10: skin-anti-pigmentation"
    }

    void 'it should trim and correct-ify steps strings with only one step'() {
        given:
        def stepsAsString = 'Step 1: some step'

        when:
        def steps = RoutineStepsUtils.justifyStepsString(stepsAsString)

        then:
        steps == "step 1: some-step"
    }

    void 'it should return a list of steps as rows'() {
        given:
        def stepsAsString = 'step 1: some step step 2: other step     step 3: roger.'

        when:
        def steps = RoutineStepsUtils.toStepsSet(stepsAsString)

        then:
        steps
        steps.size() == 3
        steps[0] == 'step 1: some-step'
        steps[1] == 'step 2: other-step'
        steps[2] == 'step 3: roger'
    }

    void 'it should include carriage returns'() {
        given:
        def stepsAsString = 'step 1: first\nstep 2: second'

        when:
        def steps = RoutineStepsUtils.toStepsSet(stepsAsString)

        then:
        steps
        steps.size() == 2
        steps[0] == 'step 1: first'
        steps[1] == 'step 2: second'
    }

    void 'it should ignore duplicate invalid steps'() {
        given:
        def stepsAsString = 'step 1: first\nstep 2: step 2:second'

        when:
        def steps = RoutineStepsUtils.toStepsSet(stepsAsString)

        then:
        steps
        steps.size() == 2
        steps[0] == 'step 1: first'
        steps[1] == 'step 2: second'
    }

    void 'it should return a list of steps in the correct order'() {
        given:
        def stepsAsString = 'step 1: some step step 2: other step     step 3: roger.'

        when:
        def steps = RoutineStepsUtils.toSteps(stepsAsString)

        then:
        steps
        steps.size() == 3
        steps[0].getSequence() == 1
        steps[0].getSkuId() == 'some-step'

        and:
        steps[1].getSequence() == 2
        steps[1].getSkuId() == 'other-step'

        and:
        steps[2].getSequence() == 3
        steps[2].getSkuId() == 'roger'
    }
}
