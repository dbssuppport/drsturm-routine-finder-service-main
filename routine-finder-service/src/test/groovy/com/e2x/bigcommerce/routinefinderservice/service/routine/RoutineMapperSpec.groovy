package com.e2x.bigcommerce.routinefinderservice.service.routine

import com.e2x.bigcommerce.routinefinder.data.SkuDefinition
import com.e2x.bigcommerce.routinefinder.data.SkuDefinitionRepository
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire
import com.e2x.bigcommerce.routinefinder.enquiry.Routine
import com.e2x.bigcommerce.routinefinder.enquiry.Step
import spock.lang.Specification

class RoutineMapperSpec extends Specification {

    RoutineMapper testObj

    SkuDefinitionRepository skuDefinitionRepository

    void setup() {
        skuDefinitionRepository = Mock()

        testObj = new RoutineMapper(skuDefinitionRepository)
    }

    void 'it can map the routine steps and skus'() {
        given:
        def step1 = new Step(1, 'body cream')
        def step2 = new Step(2, 'cleanser')

        and:
        def routine = new Routine(1)
        routine.addStep(step1)
        routine.addStep(step2)

        and:
        def sku1Id = new SkuDefinition('08-100-BOD', '08-100-BOD', '08-100-BOD')
        skuDefinitionRepository.findBy('a-store', step1.skuId) >> Optional.of(sku1Id)
        def sku2Id = new SkuDefinition('05-300-CLEANSER', '05-300-CLEANSER', '05-300-CLEANSER')
        skuDefinitionRepository.findBy('a-store', step2.skuId) >> Optional.of(sku2Id)

        and:
        def questionnaire = new Questionnaire(routines: [routine])

        when:
        def result = testObj.mapFrom('a-store', questionnaire)

        then:
        result

        and:
        result.size() == 1
        result.first().steps.size() == 2
        result.first().steps[0].sku.id == sku1Id.skuId
        result.first().steps[1].sku.id == sku2Id.skuId
    }

    void 'it can map more than one routine'() {
        given:
        def routine = new Routine(1)
        def questionnaire = new Questionnaire(routines: [routine, routine])

        when:
        def result = testObj.mapFrom('a-store', questionnaire)

        then:
        result

        and:
        result.size() == 2
    }


    void 'when the sku is not found it is not added'() {
        given:
        def step1 = new Step(1, 'body cream')
        def step2 = new Step(2, 'cleanser')

        and:
        def routine = new Routine(1)
        routine.addStep(step1)
        routine.addStep(step2)

        and:
        def sku1Id = new SkuDefinition('08-100-BOD', '08-100-BOD', '08-100-BOD')
        skuDefinitionRepository.findBy('a-store', step1.skuId) >> Optional.of(sku1Id)
        skuDefinitionRepository.findBy('a-store', step2.skuId) >> Optional.ofNullable(null)

        and:
        def questionnaire = new Questionnaire(routines: [routine])

        when:
        def result = testObj.mapFrom('a-store', questionnaire)

        then:
        result

        and:
        result.size() == 1
        result.first().steps.size() == 1
        result.first().steps[0].sku.id == sku1Id.skuId
    }
}
