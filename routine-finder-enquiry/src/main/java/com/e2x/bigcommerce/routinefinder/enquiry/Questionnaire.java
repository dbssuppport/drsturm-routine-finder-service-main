package com.e2x.bigcommerce.routinefinder.enquiry;

import com.e2x.bigcommerce.routinefinder.antlr.QuestionAnswerFinder;
import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Questionnaire extends LinkedHashSet<Question> implements QuestionAnswerFinder {
    @SuppressFBWarnings(value = "SE_BAD_FIELD", justification = "indexedQuestions is serializable")
    private final HashMap<String, Question> indexedQuestions = new HashMap<>();
    @Getter
    @Setter
    private List<Routine> routines = Lists.newArrayList();

    @Override
    public boolean add(Question question) {
        if (log.isDebugEnabled()) {
            log.debug("adding question " + question.getId());
        }

        indexedQuestions.put(question.getId(), question);

        return super.add(question);
    }

    @Override
    public List<Object> findAnswersFor(String questionId) {
        return findAnswersBy(questionId);
    }

    public void askQuestion(Question question) {
        add(question);
    }

    public boolean isOutstanding() {
        return isEmpty() || stream().anyMatch(Question::isOutstanding);
    }

    public Optional<Question> findQuestionBy(String questionId) {
        return Optional
                .ofNullable(indexedQuestions.get(questionId));
    }

    public List<Object> findAnswersBy(String questionId) {
        return findQuestionBy(questionId)
                .map(Question::getAnswers)
                .orElse(null);
    }

    public void addRoutine(Routine routine) {
        routines.add(routine);
    }
}
