package com.e2x.bigcommerce.routinefinder.antlr;

import java.util.List;

public interface QuestionAnswerFinder {
    List<Object> findAnswersFor(String questionId);
}
