package com.e2x.bigcommerce.routinefinderservice.controller

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import com.e2x.bigcommerce.routinefinderservice.ApiIntegrationSpecification

class RoutineFinderControllerScenariosSpec extends ApiIntegrationSpecification {
    def "a get request will start an enquiry, which should return a new enquiry which is not complete"() {
        when:
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()

        then:
        !routineEnquiry.complete
    }

    def "Answering the age question can result in a routine being returned"() {
        when:
        RoutineEnquiry routineEnquiry = startRoutineEnquiry('1', '1')

        then:
        !routineEnquiry.complete

        when:
        answerAgeQuestion(routineEnquiry, '18-24')
        answerQuestions(routineEnquiry, "first set")

        and:
        RoutineEnquiry updatedRoutineEnquiry = advanceRoutineEnquiry(routineEnquiry)

        then:
        updatedRoutineEnquiry.complete
        updatedRoutineEnquiry.routines

        and:
        checkAnswersSameAsInRequest(updatedRoutineEnquiry, routineEnquiry, "first set")
    }

    def "answering a question differently can result in an extra question rather than routine being returned"() {
        when:
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()

        then:
        !routineEnquiry.complete
        !routineEnquiry.routines

        when:
        answerAgeQuestion(routineEnquiry, '25-34')
        answerQuestions(routineEnquiry, "first set")

        and:
        RoutineEnquiry advancedRoutineEnquiry = advanceRoutineEnquiry(routineEnquiry)

        then:
        !advancedRoutineEnquiry.complete

        and:
        checkAnswersSameAsInRequest(advancedRoutineEnquiry, routineEnquiry, "first set")

        and:
        !advancedRoutineEnquiry.routines

        and:
        advancedRoutineEnquiry.questions.size() == routineEnquiry.questions.size() + 1
        advancedRoutineEnquiry.questions[4].options[0].code
        advancedRoutineEnquiry.questions[4].options[1].code
    }

    def "answering a questions differently can return the same extra question but with different options"() {
        when:
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()
        def advancedRoutineEnquiry = answerAndAdvance(routineEnquiry, '35-44')

        then:
        !advancedRoutineEnquiry.complete

        when:
        routineEnquiry = startRoutineEnquiry()
        def secondAdvancedRoutineEnquiry = answerAndAdvance(routineEnquiry, '25-34')

        then:
        advancedRoutineEnquiry.questions.size() == routineEnquiry.questions.size() + 1
        advancedRoutineEnquiry.questions[4].options[0].code == secondAdvancedRoutineEnquiry.questions[4].options[0].code
        advancedRoutineEnquiry.questions[4].options[1].code == secondAdvancedRoutineEnquiry.questions[4].options[1].code
        advancedRoutineEnquiry.questions[4].options.size() == secondAdvancedRoutineEnquiry.questions[4].options.size() + 1
    }

    def "when answering all extra questions return a routine"() {
        when:
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()

        then:
        !routineEnquiry.complete

        when:
        answerAgeQuestion(routineEnquiry, '25-34')
        answerQuestions(routineEnquiry, "first set")
        RoutineEnquiry advancedRoutineEnquiry = advanceRoutineEnquiry(routineEnquiry)

        then:
        !advancedRoutineEnquiry.complete

        when:
        answerQuestions(advancedRoutineEnquiry, "second set")
        RoutineEnquiry secondAdvancedRoutineEnquiry = advanceRoutineEnquiry(advancedRoutineEnquiry)

        then:
        !secondAdvancedRoutineEnquiry.complete

        and:
        checkAnswersSameAsInRequest(advancedRoutineEnquiry, secondAdvancedRoutineEnquiry, "second set")
        secondAdvancedRoutineEnquiry.questions[5].id == "routine type"

        when:
        answerRoutineSelection(secondAdvancedRoutineEnquiry, "basic")
        RoutineEnquiry completedRoutineEnquiry = advanceRoutineEnquiry(secondAdvancedRoutineEnquiry)

        then:
        completedRoutineEnquiry.complete
        completedRoutineEnquiry.routines
    }

    def "selecting different options for skin concerns will get different routines"() {
        given:
        def answerAge = "25-34"
        def answerRoutineType = "basic"

        when:
        def RoutineEnquiryPollution = getRoutineEnquiry(answerAge, "pollution", answerRoutineType)

        then:
        RoutineEnquiryPollution.complete

        when:
        def RoutineEnquiryNotPollution = getRoutineEnquiry(answerAge, "sun", answerRoutineType)

        then:
        RoutineEnquiryNotPollution.complete
        isDifferentRoutine(RoutineEnquiryPollution, RoutineEnquiryNotPollution)
    }

    def "selecting different options for age will get different routines"() {
        given:
        def answerSkinConcerns = "pollution"
        def answerRoutineType = "basic"

        when:
        def routine = getRoutineEnquiry("25-34", answerSkinConcerns, answerRoutineType)

        then:
        routine.complete

        when:
        def differentRoutine = getRoutineEnquiry("35-44", answerSkinConcerns, answerRoutineType)

        then:
        differentRoutine.complete
        isDifferentRoutine(routine, differentRoutine)
    }

    def "When select different option for routine type will give you different routines"() {
        given:
        def answerSkinConcerns = "pollution"
        def answerAge = "25-34"

        when:
        def routineEnquiryEnhanced = getRoutineEnquiry(answerAge, answerSkinConcerns, "enhanced")

        then:
        routineEnquiryEnhanced.complete

        when:
        def basicRoutineEnquiry = getRoutineEnquiry("25-34", "pollution", "basic")

        then:
        isDifferentRoutine(basicRoutineEnquiry, routineEnquiryEnhanced)
    }

    static def answerAgeQuestion(routineEnquiry, age) {
        routineEnquiry.questions[0].answers = [age]
    }

    static def answerQuestions(routineEnquiry, set, optionsSkinConcerns = "pollution") {
        if (set == "first set") {
            routineEnquiry.questions[1].answers.add('dry')
            routineEnquiry.questions[2].answers.add('light')
            routineEnquiry.questions[3].answers.add('4')
        } else if (set == "second set") {
            routineEnquiry.questions[4].answers.add(optionsSkinConcerns)
        }
    }

    static def answerRoutineSelection(routineEnquiry, routineType) {
        routineEnquiry.questions[5].answers.add(routineType)
    }

    static void checkAnswersSameAsInRequest(response, request, questionSet) {
        if (questionSet == "first set") {
            assert response.questions[0].answers == request.questions[0].answers
            assert response.questions[1].answers == request.questions[1].answers
            assert response.questions[2].answers == request.questions[2].answers
            assert response.questions[3].answers == request.questions[3].answers
        }

        if (questionSet == "second set") {
            assert response.questions[4].answers == request.questions[4].answers
        }
    }

    static def isDifferentRoutine(routine1, routine2 ) {
        def routine1Steps = routine1.routines.first().steps
        def routine2Steps = routine2.routines.first().steps
        if (routine1Steps.size() != routine2Steps.size()) {
            return true
        }
        for (int i = 0; i < routine1Steps.size(); i++) {
            if (routine1.routines.first().steps[i].sku.id != routine2.routines.first().steps[i].sku.id) {
                return true
            }
        }
        return false
    }

    RoutineEnquiry getRoutineEnquiry(age, answerSkinConcerns = 'pollution', answerRoutineType = 'basic') {
        RoutineEnquiry routineEnquiry = startRoutineEnquiry()

        answerAgeQuestion(routineEnquiry, age)
        answerQuestions(routineEnquiry, "first set")

        routineEnquiry = advanceRoutineEnquiry(routineEnquiry)

        answerQuestions(routineEnquiry, "second set", answerSkinConcerns)

        routineEnquiry = advanceRoutineEnquiry(routineEnquiry)

        answerRoutineSelection(routineEnquiry, answerRoutineType)

        advanceRoutineEnquiry(routineEnquiry)
    }

    RoutineEnquiry answerAndAdvance(routineEnquiry, age) {
        answerAgeQuestion(routineEnquiry, age)
        answerQuestions(routineEnquiry, 'first set')

        advanceRoutineEnquiry(routineEnquiry)
    }

}
