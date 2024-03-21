package com.e2x.bigcommerce.routinefinder.enquiry.graph.validator;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraphErrorReporting;
import com.e2x.bigcommerce.routinefinder.enquiry.VertexValidator;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.OptionValueNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.QuestionNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.SkuNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;
import com.google.common.collect.Maps;

import java.util.Map;

public class RoutineGraphVerticesValidationFactory {

    private RoutineGraphErrorReporting routineGraphErrorReporting;
    private SkuNodeRepository skuNodeRepository;
    private QuestionNodeRepository questionNodeRepository;
    private OptionValueNodeRepository optionValueNodeRepository;
    private ConditionExpressionValidator conditionExpressionValidator;

    private RoutineGraphVerticesValidationFactory() {
    }

    public static RoutineGraphVerticesValidationFactory newValidator() {
        return new RoutineGraphVerticesValidationFactory();
    }

    public RoutineGraphVerticesValidationFactory with(RoutineGraphErrorReporting routineGraphErrorReporting) {
        this.routineGraphErrorReporting = routineGraphErrorReporting;
        return this;
    }

    public RoutineGraphVerticesValidationFactory with(QuestionNodeRepository questionNodeRepository) {
        this.questionNodeRepository = questionNodeRepository;
        return this;
    }

    public RoutineGraphVerticesValidationFactory with(OptionValueNodeRepository optionValueNodeRepository) {
        this.optionValueNodeRepository = optionValueNodeRepository;
        return this;
    }

    public RoutineGraphVerticesValidationFactory with(SkuNodeRepository skuNodeRepository) {
        this.skuNodeRepository = skuNodeRepository;
        return this;
    }

    public RoutineGraphVerticesValidationFactory with(ConditionExpressionValidator conditionExpressionValidator) {
        this.conditionExpressionValidator = conditionExpressionValidator;
        return this;
    }

    public RoutineGraphVerticesValidationDelegator instantiate() {
        var validatorsMap = createValidators();

        return new RoutineGraphVerticesValidationDelegator(validatorsMap);
    }

    private Map<VertexType, VertexValidator>  createValidators() {
        Map<VertexType, VertexValidator> verticesValidatorMap = Maps.newHashMap();

        verticesValidatorMap.put(VertexType.QUESTION, new QuestionNodeValidator(routineGraphErrorReporting, questionNodeRepository));
        verticesValidatorMap.put(VertexType.OPTION_VALUE, new OptionValueNodeValidator(routineGraphErrorReporting, optionValueNodeRepository));
        verticesValidatorMap.put(VertexType.OPTIONS, new OptionsNodeValidator(routineGraphErrorReporting));
        verticesValidatorMap.put(VertexType.CONDITION, new ConditionExpressionNodeValidator(routineGraphErrorReporting, conditionExpressionValidator));
        verticesValidatorMap.put(VertexType.ROUTINE, new RoutineNodeValidator(routineGraphErrorReporting, skuNodeRepository));

        return verticesValidatorMap;
    }
}
