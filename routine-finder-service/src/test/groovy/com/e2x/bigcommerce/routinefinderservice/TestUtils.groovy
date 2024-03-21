package com.e2x.bigcommerce.routinefinderservice

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry
import org.assertj.core.util.Lists

class TestUtils {
    static void answer(RoutineEnquiry routineEnquiry, String questionId, String answer, boolean clearAnswers = true) {
        routineEnquiry
                .questions
                .stream()
                .filter({ q -> q.id == questionId })
                .findFirst()
                .ifPresent({ q ->
                    if (clearAnswers) {
                        q.answers = Lists.newArrayList()
                    }

                    q.answers.add(answer)
                })
    }

}
