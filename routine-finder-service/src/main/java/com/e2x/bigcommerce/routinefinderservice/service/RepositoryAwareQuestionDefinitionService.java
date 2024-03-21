package com.e2x.bigcommerce.routinefinderservice.service;

import com.e2x.bigcommerce.routinefinder.data.OptionDefinition;
import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinition;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;

public class RepositoryAwareQuestionDefinitionService implements QuestionDefinitionService {

    private final QuestionDefinitionRepository questionDefinitionRepository;
    private final OptionDefinitionRepository optionDefinitionRepository;

    public RepositoryAwareQuestionDefinitionService(QuestionDefinitionRepository questionDefinitionRepository, OptionDefinitionRepository optionDefinitionRepository) {
        this.questionDefinitionRepository = questionDefinitionRepository;
        this.optionDefinitionRepository = optionDefinitionRepository;
    }

    @Override
    public String getQuestionTextFor(String storeId, String questionId) {
        return questionDefinitionRepository
                .findBy(storeId, questionId)
                .map(QuestionDefinition::getText)
                .orElse(null);
    }

    @Override
    public String getOptionTextFor(String storeId, String questionId, String code) {
        return optionDefinitionRepository
                .findBy(storeId, questionId, code)
                .map(OptionDefinition::getText)
                .orElse(null);
    }

    @Override
    public Integer getMaxAllowedAnswerFor(String storeId, String questionId) {
        return questionDefinitionRepository
                .findBy(storeId, questionId)
                .map(QuestionDefinition::getMaxAllowedAnswers)
                .orElse(0);
    }

    @Override
    public String getQuestionTypeFor(String storeId, String questionId) {
        return questionDefinitionRepository
                .findBy(storeId, questionId)
                .map(QuestionDefinition::getType)
                .orElse(null);
    }
}
