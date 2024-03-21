package com.e2x.bigcommerce.routinefinderservice.service;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class ResourceBundleQuestionDefinitionService implements QuestionDefinitionService {

    private final ResourceBundleMessageSource resourceBundleMessageSource;

    public ResourceBundleQuestionDefinitionService(ResourceBundleMessageSource resourceBundleMessageSource) {
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    @Override
    public String getQuestionTextFor(String storeId, String questionId) {
        return getMessageBundleValueFor(storeId, String.format("%s.text", stripWhitespace(questionId)));
    }

    @Override
    public String getOptionTextFor(String storeId, String questionId, String code) {
        return getMessageBundleValueFor(storeId, String.format("%s.option.%s.text", stripWhitespace(questionId), stripWhitespace(code)));
    }

    @Override
    public Integer getMaxAllowedAnswerFor(String storeId, String questionId) {
        var value = getMessageBundleValueFor(storeId, String.format("%s.max-allowed-answers", stripWhitespace(questionId)));

        return Integer.valueOf(value);
    }

    @Override
    public String getQuestionTypeFor(String storeId, String questionId) {
        return getMessageBundleValueFor(storeId, String.format("%s.type", stripWhitespace(questionId)));
    }

    public String getMessageBundleValueFor(String storeId, String value) {
        return resourceBundleMessageSource.getMessage(value, null, new Locale(storeId, ""));
    }

    private static String stripWhitespace(String value) {
        return value.replaceAll("\\s+", "-");
    }
}
