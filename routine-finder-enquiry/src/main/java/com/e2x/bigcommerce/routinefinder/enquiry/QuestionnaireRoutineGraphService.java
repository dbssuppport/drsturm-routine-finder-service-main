package com.e2x.bigcommerce.routinefinder.enquiry;

import com.e2x.bigcommerce.routinefinder.enquiry.graph.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuestionnaireRoutineGraphService {
    private final GraphIteratorFactory graphIteratorFactory;

    public QuestionnaireRoutineGraphService() {
        this(new DepthFirstGraphIteratorFactory());
    }

    public QuestionnaireRoutineGraphService(GraphIteratorFactory graphIteratorFactory) {
        this.graphIteratorFactory = graphIteratorFactory;
    }

    public Questionnaire advance(RoutineGraph routineGraph, Questionnaire questionnaire) {
        var routineEnquiryGraph = new QuestionnaireAwareRoutineGraph(questionnaire, routineGraph);
        var graphIterator = graphIteratorFactory.createIterator(routineEnquiryGraph);
        var routineNodeProcessor = new RoutineNodeProcessor(routineEnquiryGraph);

        var advancingQuestionnaire = new Questionnaire();

        while (graphIterator.hasNext()) {
            var vertex = graphIterator.next();

            routineNodeProcessor.process(vertex, questionnaire, advancingQuestionnaire);
        }

        return advancingQuestionnaire;
    }
}
