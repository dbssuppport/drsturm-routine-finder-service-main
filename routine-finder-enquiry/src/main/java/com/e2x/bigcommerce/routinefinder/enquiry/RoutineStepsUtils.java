package com.e2x.bigcommerce.routinefinder.enquiry;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.e2x.bigcommerce.routinefinder.enquiry.TextUtils.trim;
import static com.google.common.base.CharMatcher.is;

@Slf4j
public class RoutineStepsUtils {
    public static String justifyStepsString(String stepsToJustify) {
        String lowerCaseTrimmedSteps = is('\u00A0')
                .replaceFrom(stepsToJustify, ' ')
                .toLowerCase();

        return trim(toStepsSet(lowerCaseTrimmedSteps)
                .stream()
                .reduce("", (steps, element) -> String.format("%s %s", steps, element)));
    }

    public static Set<String> toStepsSet(String stringSteps) {
        AtomicInteger index = new AtomicInteger(0);
        String stringStepsProcessed = stringSteps
                .replaceAll("step 1: ", "")
                .replaceAll("\\n", " ")
                .replaceAll("\\r", " ");


        return Stream
                .of(stringStepsProcessed.split("\\s?step\\s\\d+:\\s?"))
                .map(TextUtils::trim)
                .map((step) -> is(' ').replaceFrom(step, '-'))
                .map((step) -> is('.').trimFrom(step))
                .filter(step -> {
                    if (Strings.isNullOrEmpty(step)) {
                        log.warn(String.format("empty step found on %s", stringSteps));
                    }

                    return !Strings.isNullOrEmpty(step);
                })
                .map((step) -> String.format("step %s: %s", index.addAndGet(1), step))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Step> toSteps(String stringSteps) {
        return toStepsSet(stringSteps)
                .stream()
                .map(s -> mapStep(trim(s)))
                .sorted(Comparator.comparing(Step::getSequence))
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }

    private static Step mapStep(String step) {
        var stepNumber = step.substring(5, step.indexOf(':'));
        Integer sequence = Integer.valueOf(trim(stepNumber));
        String skuId = trim(step.substring(step.indexOf(':') + 1));

        return new Step(sequence, skuId);
    }
}
