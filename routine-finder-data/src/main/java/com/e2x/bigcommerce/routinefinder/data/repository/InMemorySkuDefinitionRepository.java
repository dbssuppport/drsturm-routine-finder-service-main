package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.SkuDefinition;
import com.e2x.bigcommerce.routinefinder.data.SkuDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.SkuNode;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.SkuNodeRepository;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * Should only use this class for tests and use StoreAwareSkuDefinitionRepository class instead.
 */
public class InMemorySkuDefinitionRepository implements SkuDefinitionRepository, SkuNodeRepository {
    private static final Map<String, SkuDefinition> SKU_DEFINITION_MAP = Maps.newConcurrentMap();

    public void save(SkuDefinition skuDefinition) {
        SKU_DEFINITION_MAP.put(skuDefinition.getName().toLowerCase(), skuDefinition);
    }

    @Override
    public Optional<SkuDefinition> findBy(String storeId, String name) {
        return Optional.ofNullable(SKU_DEFINITION_MAP.get(name.toLowerCase()));
    }

    @Override
    public Optional<SkuNode> findNodeBy(String code) {
        // store id will not be supported here.
        return findBy(null, code)
                .map(p -> new SkuNode(p.getName()));
    }
}
