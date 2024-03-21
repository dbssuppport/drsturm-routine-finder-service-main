package com.e2x.bigcommerce.routinefinder.enquiry;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
public class Routine {
    private final long id;
    private final Set<Step> steps = Sets.newLinkedHashSet();

    public Routine(long id) {
        this.id = id;
    }

    public void addStep(Step step) {
        steps.add(step);
    }
}
