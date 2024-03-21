package com.e2x.bigcommerce.routinefinderservice.service;

public interface QuestionDefinitionService {
    String getQuestionTextFor(String storeId, String questionId);
    String getOptionTextFor(String storeId, String questionId, String code);
    Integer getMaxAllowedAnswerFor(String storeId, String questionId);
    String getQuestionTypeFor(String storeId, String questionId);
}
