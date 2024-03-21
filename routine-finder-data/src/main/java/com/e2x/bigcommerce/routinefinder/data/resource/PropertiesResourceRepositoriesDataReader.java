package com.e2x.bigcommerce.routinefinder.data.resource;

import com.e2x.bigcommerce.routinefinder.data.*;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

public class PropertiesResourceRepositoriesDataReader {
    private static final String DEFAULT = "default";

    public void load(OptionDefinitionRepository optionDefinitionRepository, String resourceName) {
        propertiesConsumer(resourceName, p ->
                p.forEach((k, v) -> {
                    if (isOptionProperty((String) k)) {
                        var optionCode = getOptionCodeFor((String) k);

                        if (optionDefinitionRepository.findBy(DEFAULT, null, optionCode).isEmpty()) {
                            var optionCodeFullPath = getOptionCodeFullPath((String) k);

                            var newDefinition = new OptionDefinition();
                            newDefinition.setCode(p.getProperty(getPropertyPathFor(optionCodeFullPath, "%s.code")));
                            newDefinition.setText(p.getProperty(getPropertyPathFor(optionCodeFullPath, "%s.text")));

                            optionDefinitionRepository.save(DEFAULT, newDefinition);
                        }
                    }
                })
        );
    }

    public void load(QuestionDefinitionRepository questionDefinitionRepository, String resourceName) {
        propertiesConsumer(resourceName, p ->
                p.forEach((k, v) -> {
                    String questionId = getQuestionIdFor((String) k);

                    if (questionDefinitionRepository.findBy(DEFAULT, questionId).isEmpty()) {
                        var newQuestionDefinition = new QuestionDefinition();
                        newQuestionDefinition.setId(questionId);

                        newQuestionDefinition.setText(p.getProperty(getPropertyPathFor(questionId, "%s.text")));
                        newQuestionDefinition.setName(p.getProperty(getPropertyPathFor(questionId, "%s.name")));
                        newQuestionDefinition.setMaxAllowedAnswers(Integer.parseInt(p.getProperty(getPropertyPathFor(questionId, "%s.max-allowed-answers"))));
                        newQuestionDefinition.setType(p.getProperty(getPropertyPathFor(questionId, "%s.type")));

                        questionDefinitionRepository.save(DEFAULT, newQuestionDefinition);
                    }
                })
        );
    }

    public void load(SkuDefinitionRepository skuDefinitionRepository, String resourceName) {
        propertiesConsumer(resourceName, p ->
                p.forEach((k, v) -> {
                    String skuName = getSkuNameFor((String) k);
                    if (skuDefinitionRepository.findBy(DEFAULT, skuName).isEmpty()) {
                        var skuId = p.getProperty(getPropertyPathFor(skuName, "%s.sku-id"));
                        var skuDefinition = new SkuDefinition(DEFAULT, skuId, skuName);

                        skuDefinitionRepository.save(skuDefinition);
                    }
                })
        );
    }

    private String getPropertyPathFor(String questionId, String s) {
        return String.format(s, questionId.replaceAll(" ", "-"));
    }

    private void propertiesConsumer(String resourceName, Consumer<Properties> propertiesConsumer) {
        var inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);

        try {
            Properties properties = new Properties();
            properties.load(inputStream);

            propertiesConsumer.accept(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getOptionCodeFor(String property) {
        return getPaths(property)[2];
    }

    private String getOptionCodeFullPath(String property) {
        var paths = getPaths(property);

        return paths[0] + "." + paths[1] + "." + paths[2];
    }

    private boolean isOptionProperty(String property) {
        return property.contains(".option");
    }

    private String getQuestionIdFor(String k) {
        return getPaths(k)[0].replaceAll("-", " ");
    }

    private String getSkuNameFor(String k) {
        return getPaths(k)[0];
    }

    private String[] getPaths(String property) {
        return property.split("\\.");
    }

}
