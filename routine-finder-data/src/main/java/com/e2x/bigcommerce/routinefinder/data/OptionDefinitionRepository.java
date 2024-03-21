package com.e2x.bigcommerce.routinefinder.data;

import java.util.Optional;

public interface OptionDefinitionRepository {
    void save(String storeId, OptionDefinition optionDefinition);
    Optional<OptionDefinition> findBy(String storeId, String questionId, String code);
}
