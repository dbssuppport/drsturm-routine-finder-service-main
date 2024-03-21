package com.e2x.bigcommerce.routinefinder.cli.verification;

import com.google.api.client.util.Sets;
import lombok.Data;

import java.util.Set;

@Data
public class AllowedChoices {
    private final String questionId;
    private final Set<Object> choices = Sets.newHashSet();

    public void addChoice(Object object) {
        choices.add(object);
    }
}
