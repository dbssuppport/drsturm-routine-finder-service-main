package com.e2x.bigcommerce.routinefinder.cli.verification;

import com.e2x.bigcommerce.routinefinder.enquiry.Step;
import com.google.api.client.util.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Scenario {
    private Map<String, AllowedChoices> conditionMap = Maps.newHashMap();
    private Set<Step> steps = Sets.newLinkedHashSet();

    public void addCriteria(AllowedChoices allowedChoices) {
        conditionMap.put(allowedChoices.getQuestionId(), allowedChoices);
    }

    public Optional<AllowedChoices> getAllowedChoicesFor(String questionId) {
        return Optional.ofNullable(conditionMap.get(questionId));
    }

    public List<AllowedChoices> getAllAllowedChoices() {
        return new ArrayList<>(conditionMap.values());
    }
}

