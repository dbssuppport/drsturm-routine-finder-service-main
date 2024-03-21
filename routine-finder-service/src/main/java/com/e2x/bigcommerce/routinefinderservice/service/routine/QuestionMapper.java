package com.e2x.bigcommerce.routinefinderservice.service.routine;

import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.e2x.bigcommerce.routinefindermodel.Option;
import com.e2x.bigcommerce.routinefindermodel.Question;
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;
import com.e2x.bigcommerce.routinefinderservice.service.QuestionDefinitionService;
import com.google.api.client.util.Strings;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapper {

    private final QuestionDefinitionService questionDefinitionService;

    public QuestionMapper(QuestionDefinitionService questionDefinitionService) {
        this.questionDefinitionService = questionDefinitionService;
    }

    public List<Question> mapFrom(String storeId, Questionnaire questionnaire, RoutineEnquiry routineEnquiry) {
        return questionnaire
                .stream()
                .filter(q -> !Strings.isNullOrEmpty(q.getId()))
                .map(q -> {
                            var question = getOrCreateQuestionFrom(routineEnquiry, q.getId());
                            question.setText(questionDefinitionService.getQuestionTextFor(storeId, q.getId()));
                            question.setMaxAllowedAnswers(questionDefinitionService.getMaxAllowedAnswerFor(storeId, q.getId()));
                            question.setType(questionDefinitionService.getQuestionTypeFor(storeId, q.getId()));
                            question.setProgress(q.getProgress());

                            question.setOptions(q.getOptions()
                                    .stream()
                                    .map(o -> Option
                                            .builder()
                                            .code(o.getCode())
                                            .text(questionDefinitionService.getOptionTextFor(storeId, q.getId(), o.getCode()))
                                            .build()
                                    ).collect(Collectors.toList()));


                            return question;
                        }
                )
                .collect(Collectors.toList());
    }

    private Question getOrCreateQuestionFrom(RoutineEnquiry routineEnquiry, String questionId) {
        return routineEnquiry
                .getQuestions()
                .stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElse(new Question(questionId, null, null, null, Lists.newArrayList(), Lists.newArrayList(), null, null));
    }

}
