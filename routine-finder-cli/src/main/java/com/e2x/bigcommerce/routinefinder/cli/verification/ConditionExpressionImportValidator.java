package com.e2x.bigcommerce.routinefinder.cli.verification;

import com.e2x.bigcommerce.routinefinder.antlr.ConditionExpressionReaderListener;
import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.validator.ConditionExpressionValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

import static com.e2x.bigcommerce.routinefinder.antlr.ConditionExpressionWalker.walk;

@Slf4j
public class ConditionExpressionImportValidator implements ConditionExpressionValidator {
    private static final String DEFAULT = "default";

    private final QuestionDefinitionRepository questionDefinitionRepository;
    private final OptionDefinitionRepository optionDefinitionRepository;

    public ConditionExpressionImportValidator(QuestionDefinitionRepository questionDefinitionRepository, OptionDefinitionRepository optionDefinitionRepository) {
        this.questionDefinitionRepository = questionDefinitionRepository;
        this.optionDefinitionRepository = optionDefinitionRepository;
    }

    @Override
    public void validate(String expression, Consumer<String> errorConsumer) {
        var conditionExpressionReader = new ConditionExpressionReaderListener();
        walk(expression, conditionExpressionReader);
        var conditionExpression = conditionExpressionReader.getEvaluatedConditionExpression();

        if (questionDefinitionRepository.findBy(DEFAULT, conditionExpression.getQuestionId()).isEmpty()) {
            errorConsumer.accept(String.format("question with id %s does not exists in question definition repository", conditionExpression.getQuestionId()));
            return;
        }

        conditionExpression.getAnswersToMatch().forEach(v -> {
            if (optionDefinitionRepository.findBy(DEFAULT, null, (String) v).isEmpty()) {
                errorConsumer.accept(String.format("option with code %s does not exists in option definition repository", v));
            }
        });
    }
}
