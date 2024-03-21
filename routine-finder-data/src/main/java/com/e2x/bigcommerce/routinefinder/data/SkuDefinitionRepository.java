package com.e2x.bigcommerce.routinefinder.data;

import java.util.Optional;

public interface SkuDefinitionRepository {
    Optional<SkuDefinition> findBy(String storeId, String name);
    void save(SkuDefinition skuDefinition);
}
