package com.e2x.bigcommerce.routinefinder.data;

import java.util.List;
import java.util.Optional;

public interface QuestionDefinitionRepository {
    Optional<QuestionDefinition> findBy(String storeId, String questionId);
    void save(String storeId, QuestionDefinition questionDefinition);
    List<QuestionDefinition> getAll(String storeId);
}
