package com.e2x.bigcommerce.routinefinder.cli.verification;

import com.e2x.bigcommerce.routinefinder.antlr.ConditionExpressionReaderListener;
import com.e2x.bigcommerce.routinefinder.antlr.ConditionExpressionWalker;
import com.e2x.bigcommerce.routinefinder.data.SkuDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.Step;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.e2x.bigcommerce.routinefinder.enquiry.RoutineStepsUtils.toSteps;
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.codeFor;
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.stepsFor;

public class ScenarioFactory {

    private final SkuDefinitionRepository skuDefinitionRepository;

    public ScenarioFactory(SkuDefinitionRepository skuDefinitionRepository) {
        this.skuDefinitionRepository = skuDefinitionRepository;
    }

    public Scenario createScenarioFor(RoutineGraphPath<Vertex, Edge> path, RoutineGraph routineGraph) {
        var scenario = new Scenario();

        path.getVertexList().forEach(vertex -> {
            if (VertexType.CONDITION.equals(vertex.getVertexType())) {

                var allowedChoices = createAllowedChoicesFor(vertex, routineGraph);
                scenario.addCriteria(allowedChoices);
            }

            if (VertexType.ROUTINE.equals(vertex.getVertexType())) {
                scenario.setSteps(getRoutineStepsFor(vertex));
            }
        });

        return scenario;
    }

    private Set<Step> getRoutineStepsFor(Vertex vertex) {
        return toSteps(stepsFor(vertex))
                .stream()
                .map(s -> new Step(s.getSequence(), skuDefinitionRepository.findBy("", s.getSkuId()).get().getSkuId()))
                .collect(Collectors.toSet());
    }

    private AllowedChoices createAllowedChoicesFor(Vertex vertex, RoutineGraph routineGraph) {
        var conditionExpressionReader = new ConditionExpressionReaderListener();
        ConditionExpressionWalker.walk(VertexUtils.expressionFor(vertex), conditionExpressionReader);
        var conditionExpression = conditionExpressionReader.getEvaluatedConditionExpression();

        var allowedChoices = new AllowedChoices(conditionExpression.getQuestionId());

        if (conditionExpression.isNegated()) {
            findOptionsFor(vertex, routineGraph)
                    .forEach(allowedChoices::addChoice);

        } else {
            conditionExpression
                    .getAnswersToMatch()
                    .forEach(allowedChoices::addChoice);
        }

        return allowedChoices;
    }

    private List<Object> findOptionsFor(Vertex vertex, RoutineGraph routineGraph) {
        if (!VertexType.QUESTION.equals(vertex.getVertexType())) {
            throw new IllegalArgumentException(String.format("cannot find question options on vertex of type %s", vertex.getVertexType()));
        }

        return routineGraph
                .findChildVerticesFor(vertex)
                .stream()
                .filter(childVertex -> VertexType.OPTIONS.equals(childVertex.getVertexType()))
                .flatMap(optionsVertex -> routineGraph.findChildVerticesFor(optionsVertex).stream().map(optionVertex -> codeFor(optionsVertex)))
                .collect(Collectors.toList());
    }
}
