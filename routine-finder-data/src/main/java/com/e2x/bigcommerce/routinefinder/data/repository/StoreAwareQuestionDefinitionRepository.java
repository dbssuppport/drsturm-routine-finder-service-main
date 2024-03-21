package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.QuestionDefinition;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.QuestionNodeRepository;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor;

public class StoreAwareQuestionDefinitionRepository implements QuestionDefinitionRepository, QuestionNodeRepository {
    private static final String BUNDLE_RESOURCE_NAME = "question-definition";
    private static final String DEFAULT = "default";

    @Override
    public Optional<QuestionDefinition> findBy(String storeId, String questionId) {
        var resourceBundle = ResourceBundle.getBundle(BUNDLE_RESOURCE_NAME, new Locale(storeId, ""));

        var questionDefinition = new QuestionDefinition();
        questionDefinition.setId(questionId);

        questionDefinition.setText(resourceBundle.getString(getPropertyPathFor(questionId, "%s.text")));
        questionDefinition.setName(resourceBundle.getString(getPropertyPathFor(questionId, "%s.name")));
        questionDefinition.setMaxAllowedAnswers(Integer.parseInt(resourceBundle.getString(getPropertyPathFor(questionId, "%s.max-allowed-answers"))));
        questionDefinition.setType(resourceBundle.getString(getPropertyPathFor(questionId, "%s.type")));

        return Optional.of(questionDefinition);
    }

    @Override
    public void save(String storeId, QuestionDefinition questionDefinition) {
        throw new NotImplementedException("method not supported by this implementation");
    }

    @Override
    public List<QuestionDefinition> getAll(String storeId) {
        throw new NotImplementedException("method not supported by this implementation");
    }

    @Override
    public Optional<Object> findNodeBy(String questionId) {
        return findBy(DEFAULT, questionId)
                .map(q -> newQuestionVertexFor(questionId));
    }

    private String getPropertyPathFor(String questionId, String s) {
        return String.format(s, questionId.replaceAll(" ", "-"));
    }

}
