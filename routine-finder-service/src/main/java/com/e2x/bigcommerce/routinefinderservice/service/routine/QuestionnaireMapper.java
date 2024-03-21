package com.e2x.bigcommerce.routinefinderservice.service.routine;

import com.e2x.bigcommerce.routinefinder.enquiry.Question;
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuestionnaireMapper {

    public Questionnaire mapFrom(RoutineEnquiry routineEnquiry) {
        Questionnaire questionnaire = new Questionnaire();

        mapInto(routineEnquiry, questionnaire);

        return questionnaire;
    }

    public void mapInto(RoutineEnquiry routineEnquiry, Questionnaire questionnaire) {
        routineEnquiry
                .getQuestions()
                .forEach(q -> questionnaire.add(new Question(q.getId(), q.getAnswers())));
    }
}
