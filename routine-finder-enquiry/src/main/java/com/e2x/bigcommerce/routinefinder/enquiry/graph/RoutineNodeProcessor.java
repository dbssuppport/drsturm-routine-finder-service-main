package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.enquiry.Question;
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.e2x.bigcommerce.routinefinder.enquiry.Routine;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import static com.e2x.bigcommerce.routinefinder.enquiry.RoutineStepsUtils.toSteps;
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.*;

@Slf4j
public class RoutineNodeProcessor {

    private final QuestionnaireAwareRoutineGraph questionnaireAwareRoutineGraph;

    public RoutineNodeProcessor(QuestionnaireAwareRoutineGraph questionnaireAwareRoutineGraph) {
        this.questionnaireAwareRoutineGraph = questionnaireAwareRoutineGraph;
    }

    public void process(Vertex vertex, Questionnaire questionnaire, Questionnaire advancingQuestionnaire) {
        switch (vertex.getVertexType()) {
            case QUESTION:
                processQuestionNode(vertex, questionnaire, advancingQuestionnaire);
                break;
            case OPTIONS:
                processOptionNode(vertex, advancingQuestionnaire);
                break;
            case OPTION_VALUE:
                break;
            case CONDITION:
                if (log.isDebugEnabled()) {
                    log.debug("processing condition node: " + VertexUtils.expressionFor(vertex));
                }
                break;
            case ROUTINE:
                processRoutineNode(vertex, advancingQuestionnaire);
                break;
            default:
                throw new NotImplementedException("no processor found for vertex " + vertex.getClass().getName());
        }
    }

    private void processOptionNode(Vertex vertex, Questionnaire advancingQuestionnaire) {
        if (log.isDebugEnabled()) {
            log.debug("processing options node");
        }

        questionnaireAwareRoutineGraph
                .findParentVerticesFor(vertex)
                .stream()
                .filter(v -> VertexType.QUESTION.equals(v.getVertexType()))
                .forEach(node ->
                        advancingQuestionnaire
                                .findQuestionBy(VertexUtils.questionIdFor(node))
                                .ifPresent(question -> {
                                    question.getOptions().clear();

                                    questionnaireAwareRoutineGraph
                                            .findChildVerticesFor(vertex)
                                            .stream()
                                            .filter(v -> VertexType.OPTION_VALUE.equals(v.getVertexType()))
                                            .forEach(optionValueNode -> {
                                                question.addOption(codeFor(optionValueNode));
                                            });
                                })
                );
    }

    private void processQuestionNode(Vertex vertex, Questionnaire questionnaire, Questionnaire advancingQuestionnaire) {
        if (log.isDebugEnabled()) {
            log.debug("processing question node: " + questionIdFor(vertex));
        }

        var question = questionnaire
                .findQuestionBy(questionIdFor(vertex))
                .orElse(new Question(questionIdFor(vertex)));

        question.setProgress(VertexUtils.progressFor(vertex));

        advancingQuestionnaire.askQuestion(question);
    }

    private void processRoutineNode(Vertex vertex, Questionnaire advancingQuestionnaire) {
        if (log.isDebugEnabled()) {
            log.debug("processing routine node: " + stepsFor(vertex));
        }

        var routine = new Routine(vertex.getId());

        String steps = stepsFor(vertex);
        if (!Strings.isNullOrEmpty(steps)) {
            toSteps(steps).forEach(routine::addStep);
        }

        advancingQuestionnaire.addRoutine(routine);
    }

}
