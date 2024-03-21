package com.e2x.bigcommerce.routinefinder.antlr;

import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderBaseListener;
import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderParser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class ConditionExpressionListener extends RoutineFinderBaseListener {

    private static final Supplier<Boolean> FALSE =  () -> false;

    private final QuestionAnswerFinder questionAnswerFinder;
    private final ConditionExpressionReader conditionExpressionReader = new ConditionExpressionReader();
    private Supplier<Boolean> predicate;
    private boolean isNot = false;

    public ConditionExpressionListener(QuestionAnswerFinder questionAnswerFinder) {
        super();
        this.questionAnswerFinder = questionAnswerFinder;
    }

    public boolean evaluate() {
        if (isNot) {
            return !predicate.get();
        }

        return predicate.get();
    }

    @Override
    public void enterMatch_rule(RoutineFinderParser.Match_ruleContext ctx) {
        var conditionExpression = conditionExpressionReader.read(ctx);
        var answers = getAnswersFor(conditionExpression.getQuestionId());

        if (isNullOrEmpty(answers)) {
            predicate = FALSE;
        } else {
            var valuesToMatch = conditionExpression.getAnswersToMatch();
            isNot = conditionExpression.isNegated();

            if (conditionExpression.getOperand().equals(ConditionExpression.Operand.IN)) {
                predicate = () -> anyAnswersIn(answers, valuesToMatch);
            } else {
                var answer = valueAsString(getOnlyAnswerFrom(answers, conditionExpression.getQuestionId()));
                var valueToMatch = conditionExpression.getAnswersToMatch().get(0);

                predicate = () -> valueToMatch.equals(answer);
            }
        }
    }

    private String valueAsString(Object value) {
        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }

    private boolean anyAnswersIn(List<Object> answers, List<Object> valuesToMatch) {
        return answers
                .stream()
                .map(Object::toString)
                .anyMatch(valuesToMatch::contains);
    }

    private boolean isNullOrEmpty(List<Object> values) {
        return values == null || values.isEmpty();
    }

    private Object getOnlyAnswerFrom(List<Object> answers, String questionId) {
        if (answers.size() > 1) {
            throw new RoutineFinderExpressionException(String.format("more than one answer was found on question %s", questionId));
        }

        return answers.get(0);
    }

    private List<Object> getAnswersFor(String questionId) {
        return questionAnswerFinder.findAnswersFor(questionId);
    }

}
