package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.OptionDefinition;
import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.OptionValueNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex;

@Slf4j
public class InMemoryOptionDefinitionRepository implements OptionDefinitionRepository, OptionValueNodeRepository {
    private static final Map<String, Map<String, OptionDefinition>> OPTION_DEFINITION_BY_STORE_MAP = Maps.newConcurrentMap();
    private static final String DEFAULT = "default";

    @Override
    public void save(String storeId, OptionDefinition optionDefinition) {
        var storeMap = getStoreMap(getStoreIdOrDefault(storeId));

        storeMap.put(optionDefinition.getCode(), optionDefinition);
    }

    @Override
    public Optional<OptionDefinition> findBy(String storeId, String questionId, String code) {
        return Optional.ofNullable(onStoreMap(storeId, m -> m.get(code)));
    }

    @Override
    public Optional<Vertex> findNodeBy(String code) {
        return findBy(DEFAULT, null, code)
                .map(o -> newOptionValueVertex(o.getCode()));
    }

    private <T> T onStoreMap(String storeId, Function<Map<String, OptionDefinition>, T> consumer) {
        var storeHashId = getStoreIdOrDefault(storeId);
        var storeMap = getStoreMap(storeHashId);

        var valueToReturn = consumer.apply(storeMap);

        if (valueToReturn == null && !DEFAULT.equals(storeHashId)) {
            return onStoreMap(DEFAULT, consumer);
        }

        return valueToReturn;
    }

    private Map<String, OptionDefinition> getStoreMap(String storeId) {
        if (!OPTION_DEFINITION_BY_STORE_MAP.containsKey(storeId)) {
            OPTION_DEFINITION_BY_STORE_MAP.put(storeId, Maps.newConcurrentMap());
        }

        return OPTION_DEFINITION_BY_STORE_MAP.get(storeId);
    }

    private String getStoreIdOrDefault(String storeId) {
        if (Strings.isNullOrEmpty(storeId)) {
            return DEFAULT;
        }

        return storeId;
    }
}
