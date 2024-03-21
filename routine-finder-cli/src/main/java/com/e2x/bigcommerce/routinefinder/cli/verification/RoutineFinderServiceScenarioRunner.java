package com.e2x.bigcommerce.routinefinder.cli.verification;

import com.e2x.bigcommerce.routinefinder.enquiry.Step;
import com.e2x.bigcommerce.routinefindermodel.Question;
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class RoutineFinderServiceScenarioRunner implements ScenarioRunner {

    private final RoutineFinderClient routineFinderClient;

    public RoutineFinderServiceScenarioRunner(RoutineFinderClient routineFinderClient) {
        this.routineFinderClient = routineFinderClient;
    }

    @Override
    public void execute(Scenario scenario) {
        RoutineEnquiry routineEnquiry = routineFinderClient.startRoutineEnquiry();

        do {
            answerRoutineQuestions(routineEnquiry, scenario);
            routineEnquiry = routineFinderClient.submitRoutineEnquiry(routineEnquiry);
        } while (!routineEnquiry.isComplete());

        checkRoutine(routineEnquiry, scenario);
    }

    private void checkRoutine(RoutineEnquiry routineEnquiry, Scenario scenario) {
        if (routineEnquiry.getRoutines().size() == 0) {
            log.error("no routines returned for given scenario.");
        }

        routineEnquiry.getRoutines().forEach(r -> {
            if (r.getSteps().size() == 0) {
                log.error("no steps returned in routine");
            }
        });

        var expectedSkuIds = scenario
                .getSteps()
                .stream()
                .map(Step::getSkuId)
                .collect(Collectors.toList());

        if (routineEnquiry
                .getRoutines()
                .stream()
                .filter(r -> {
            var unexpectedSkuIds = r
                    .getSteps()
                    .stream()
                    .map(s -> s.getSku().getId())
                    .filter(receivedSkuId -> !expectedSkuIds.contains(receivedSkuId))
                    .collect(Collectors.toList());

            return unexpectedSkuIds.size() == 0;
        }).findAny().isEmpty()) {
            log.error("unexpected routine returned");
            log.error(routineEnquiry.toString());
        }

    }

    private void answerRoutineQuestions(RoutineEnquiry routineEnquiry, Scenario scenario) {
        routineEnquiry
                .getQuestions()
                .stream()
                .filter(q -> !q.isAnswered())
                .forEach(q -> {
                    var answer = findAnswerOrDefaultFor(q, scenario);
                    q.setAnswers(Lists.newArrayList(answer));
                });
    }

    private String findAnswerOrDefaultFor(Question question, Scenario scenario) {
        var allowedChoices = scenario
                .getAllowedChoicesFor(question.getId())
                .orElseGet(() -> {
                    var defaultChoice = new AllowedChoices(question.getId());
                    defaultChoice
                            .addChoice(question
                                    .getOptions()
                                    .stream()
                                    .findFirst()
                                    .get()
                                    .getCode()
                            );

                    return defaultChoice;
                });

        return (String) allowedChoices
                .getChoices()
                .stream()
                .findFirst()
                .orElse(null);
    }

}
