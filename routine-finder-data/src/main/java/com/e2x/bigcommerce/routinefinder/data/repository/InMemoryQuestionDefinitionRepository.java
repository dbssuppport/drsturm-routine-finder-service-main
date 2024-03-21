package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.QuestionDefinition;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.QuestionNodeRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor;

@Slf4j
public class InMemoryQuestionDefinitionRepository implements QuestionDefinitionRepository, QuestionNodeRepository {
    private static final Map<String, Map<String, QuestionDefinition>> QUESTION_ID_DEFINITION_BY_STORE_MAP = Maps.newConcurrentMap();
    private static final String DEFAULT = "default";

    @Override
    public void save(String storeId, QuestionDefinition questionDefinition) {
        var storeMap = getStoreMap(getStoreIdOrDefaultFor(storeId));
        storeMap.put(questionDefinition.getId(), questionDefinition);
    }

    @Override
    public List<QuestionDefinition> getAll(String storeId) {
        Map<String, QuestionDefinition> combinedMap = Maps.newConcurrentMap();
        combinedMap.putAll(getStoreMap(DEFAULT));

        var storeIdOrDefault = DEFAULT;
        if (!StringUtils.isBlank(storeId)) {
            storeIdOrDefault = storeId;
        }
        var storeMap = getStoreMap(storeIdOrDefault);
        combinedMap.putAll(storeMap);

        return combinedMap
                .values()
                .stream()
                .sorted(Comparator.comparing(QuestionDefinition::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<QuestionDefinition> findBy(String storeId, String questionId) {
        if (Strings.isNullOrEmpty(questionId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(onStoreMap(storeId, (m) -> m.get(questionId)));
    }

    @Override
    public Optional<Object> findNodeBy(String questionId) {
        return findBy(DEFAULT, questionId)
                .map(q -> newQuestionVertexFor(questionId));
    }

    private <T> T onStoreMap(String storeId, Function<Map<String, QuestionDefinition>, T> consumer) {
        var storeHashId = getStoreIdOrDefaultFor(storeId);
        var storeMap = getStoreMap(getStoreIdOrDefaultFor(storeHashId));

        var valueToReturn = consumer.apply(storeMap);

        if (valueToReturn == null && !DEFAULT.equals(storeHashId)) {
            return onStoreMap(DEFAULT, consumer);
        }

        return valueToReturn;
    }

    private Map<String, QuestionDefinition> getStoreMap(String storeId) {
        if (!QUESTION_ID_DEFINITION_BY_STORE_MAP.containsKey(storeId)) {
            QUESTION_ID_DEFINITION_BY_STORE_MAP.put(storeId, Maps.newConcurrentMap());
        }

        return QUESTION_ID_DEFINITION_BY_STORE_MAP.get(storeId);
    }

    private String getStoreIdOrDefaultFor(String storeId) {
        var storeHashId = storeId;
        if (ObjectUtils.isEmpty(storeId)) {
            storeHashId = DEFAULT;
        }

        return storeHashId;
    }
}
