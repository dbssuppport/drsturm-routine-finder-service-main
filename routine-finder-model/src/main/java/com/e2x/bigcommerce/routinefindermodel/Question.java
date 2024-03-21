package com.e2x.bigcommerce.routinefindermodel;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Question {
    private String id;
    private String text;
    private Integer progress;
    private Integer maxAllowedAnswers;
    private List<Option> options = Lists.newArrayList();
    private List<Object> answers = Lists.newArrayList();
    private Error error;
    private String type;

    public boolean isAnswered() {
        return answers != null && answers.size() > 0;
    }

    public boolean isValid() {
        return error == null;
    }
}
