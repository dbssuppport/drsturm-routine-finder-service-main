package com.e2x.bigcommerce.routinefinder.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDefinition {

    private String id;
    private String name;
    private String text;
    private int maxAllowedAnswers;
    private String type;
}
