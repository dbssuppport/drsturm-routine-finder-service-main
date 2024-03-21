package com.e2x.bigcommerce.routinefinderservice.service.routine;
import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.e2x.bigcommerce.routinefinder.enquiry.QuestionnaireRoutineGraphService;
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;
import com.e2x.bigcommerce.routinefinderservice.service.RoutineEnquiryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GraphRoutineService implements RoutineEnquiryService {

    private final RoutineGraphRepository routineGraphRepository;
    private final QuestionnaireRoutineGraphService questionnaireRoutineGraphService;
    private final RoutineEnquiryMapper routineEnquiryMapper;
    private final QuestionnaireMapper questionnaireMapper;

    public GraphRoutineService(RoutineGraphRepository routineGraphRepository,
                               QuestionnaireRoutineGraphService questionnaireRoutineGraphService,
                               RoutineEnquiryMapper routineEnquiryMapper,
                               QuestionnaireMapper questionnaireMapper) {
        this.routineGraphRepository = routineGraphRepository;
        this.questionnaireRoutineGraphService = questionnaireRoutineGraphService;
        this.routineEnquiryMapper = routineEnquiryMapper;
        this.questionnaireMapper = questionnaireMapper;
    }

    @Override
    public RoutineEnquiry start(String storeId) {
        var routineGraph = routineGraphRepository.fetchCurrent();

        var questionnaire = questionnaireRoutineGraphService.advance(routineGraph, new Questionnaire());

        return routineEnquiryMapper.mapFrom(storeId, questionnaire);
    }

    @Override
    public RoutineEnquiry submit(String storeId, RoutineEnquiry routineEnquiry) {
        var routineGraph = routineGraphRepository.fetchCurrent();

        var questionnaire = questionnaireMapper.mapFrom(routineEnquiry);

        var advancedQuestionnaire = questionnaireRoutineGraphService.advance(routineGraph, questionnaire);

        return routineEnquiryMapper.mapFrom(storeId, advancedQuestionnaire, routineEnquiry);
    }

}
