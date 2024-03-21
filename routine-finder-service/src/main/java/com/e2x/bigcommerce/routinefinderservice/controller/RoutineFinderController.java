package com.e2x.bigcommerce.routinefinderservice.controller;

import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinition;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;
import com.e2x.bigcommerce.routinefindermodel.Error;
import com.e2x.bigcommerce.routinefindermodel.ErrorCode;
import com.e2x.bigcommerce.routinefindermodel.Question;
import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;
import com.e2x.bigcommerce.routinefinderservice.service.RoutineEnquiryService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(path = "/bc/store/{storeHashId}/customer/{customerId}/routine", produces = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
public class RoutineFinderController {
    private final RoutineEnquiryService routineEnquiryService;
    private final QuestionDefinitionRepository questionDefinitionRepository;
    private final OptionDefinitionRepository optionDefinitionRepository;
    private final MessageSource messageSource;

    private RoutineFinderController(RoutineEnquiryService routineEnquiryService, QuestionDefinitionRepository questionDefinitionRepository, OptionDefinitionRepository optionDefinitionRepository, MessageSource messageSource) {
        this.routineEnquiryService = routineEnquiryService;
        this.questionDefinitionRepository = questionDefinitionRepository;
        this.optionDefinitionRepository = optionDefinitionRepository;
        this.messageSource = messageSource;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoutineEnquiry> advance(@PathVariable("storeHashId") String storeHashId, @PathVariable("customerId") Integer customerId, @RequestBody RoutineEnquiry routineEnquiry) {

        if (isRequestInvalid(storeHashId, routineEnquiry)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(routineEnquiry, headers, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(routineEnquiryService.submit(storeHashId, routineEnquiry), HttpStatus.OK);
    }

    @GetMapping
    public RoutineEnquiry get(@PathVariable("storeHashId") String storeHashId, @PathVariable("customerId") Integer customerId) {
        return routineEnquiryService.start(storeHashId);
    }

    private Boolean isRequestInvalid(String storeHashId, RoutineEnquiry routineEnquiry) {
        routineEnquiry
                .getQuestions()
                .forEach(question -> {
                    int maxAnswers = findMaxAnswers(storeHashId, question);

                    if (isOverMaxAnswers(question, maxAnswers)) {
                        createError(question, Integer.toString(maxAnswers), ErrorCode.MAX_ANSWER);
                    } else {
                        question.getAnswers().forEach(answer -> {
                            if (isInvalidAnswer(storeHashId, question.getId(), String.valueOf(answer))) {
                                createError(question, String.valueOf(answer), ErrorCode.INVALID_ANSWER);
                            }
                        });
                    }
                });

        return routineEnquiry
                .getQuestions()
                .stream()
                .anyMatch(q -> !q.isValid());
    }

    private void createError(Question question, String messageSourceArgs, String errorCode) {
        String messageSourceArgsPlural = "";

        if (!messageSourceArgs.equals("1")) {
            messageSourceArgsPlural = "s";
        }

        Object[] args = new Object[] { messageSourceArgs, messageSourceArgsPlural };
        String errorMessage = messageSource.getMessage(errorCode, args, errorCode, Locale.getDefault());
        Error error = new Error(errorCode, errorMessage, null);
        question.setError(error);
    }

    private int findMaxAnswers(String storeHashId, Question question) {
        return questionDefinitionRepository
                .findBy(storeHashId, question.getId())
                .map(QuestionDefinition::getMaxAllowedAnswers)
                .orElse(0);
    }

    private boolean isOverMaxAnswers(Question question, int maxAnswers) {
        return maxAnswers < question.getAnswers().size();
    }

    private boolean isInvalidAnswer(String storeHashId, String questionId, String answer) {
        return optionDefinitionRepository.findBy(storeHashId, questionId, answer).isEmpty();
    }
}
