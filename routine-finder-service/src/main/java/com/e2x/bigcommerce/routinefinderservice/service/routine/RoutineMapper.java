package com.e2x.bigcommerce.routinefinderservice.service.routine;

import com.e2x.bigcommerce.routinefinder.data.SkuDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.e2x.bigcommerce.routinefindermodel.Routine;
import com.e2x.bigcommerce.routinefindermodel.Sku;
import com.e2x.bigcommerce.routinefindermodel.Step;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RoutineMapper {

    private final SkuDefinitionRepository skuDefinitionRepository;

    public RoutineMapper(SkuDefinitionRepository skuDefinitionRepository) {
        this.skuDefinitionRepository = skuDefinitionRepository;
    }

    public List<Routine> mapFrom(String storeId, Questionnaire questionnaire) {
        return questionnaire
                .getRoutines()
                .stream()
                .map(routine -> {
                    final LinkedHashSet<Step> routineSteps = Sets.newLinkedHashSet();

                    routine
                            .getSteps()
                            .stream()
                            .sorted(Comparator.comparing(com.e2x.bigcommerce.routinefinder.enquiry.Step::getSequence))
                            .forEach(s -> {
                                String skuName = s.getSkuId();
                                skuDefinitionRepository
                                        .findBy(storeId, skuName)
                                        .map(sku -> routineSteps.add(new Step(new Sku(sku.getSkuId()))))
                                        .orElseGet(() -> logError(skuName));
                            });

                    return new Routine(UUID.randomUUID().toString(), routineSteps);
                })
                .collect(Collectors.toList());
    }

    private boolean logError(String skuName) {
        log.error(skuName + " does not match any product in our repository");
        return true;
    }
}
