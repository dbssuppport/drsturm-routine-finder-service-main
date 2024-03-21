package com.e2x.bigcommerce.routinefinderservice.service.routine;

import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoutineEnquiryMapper {

    private final QuestionMapper questionMapper;
    private final RoutineMapper routineMapper;

    public RoutineEnquiryMapper(QuestionMapper questionMapper, RoutineMapper routineMapper) {
        this.questionMapper = questionMapper;
        this.routineMapper = routineMapper;
    }

    public RoutineEnquiry mapFrom(String storeId, Questionnaire questionnaire) {
        return mapFrom(storeId, questionnaire, new RoutineEnquiry());
    }

    public RoutineEnquiry mapFrom(String storeId, Questionnaire questionnaire, RoutineEnquiry routineEnquiry) {
        routineEnquiry.setQuestions(questionMapper.mapFrom(storeId, questionnaire, routineEnquiry));
        routineEnquiry.setComplete(!questionnaire.isOutstanding());
        routineEnquiry.setRoutines(routineMapper.mapFrom(storeId, questionnaire));

        return routineEnquiry;
    }

}
