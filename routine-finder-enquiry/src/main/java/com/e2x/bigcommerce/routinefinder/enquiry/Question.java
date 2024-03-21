package com.e2x.bigcommerce.routinefinder.enquiry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(of = { "id" })
public class Question {
    private final String id;
    private final List<Object> answers;
    private Integer progress;
    private String type;
    private final Set<Option> options = Sets.newLinkedHashSet();

    public Question(String id) {
        this(id, Lists.newArrayList());
    }

    public Question(String id, List<Object> answers) {
        this.id = id;
        this.answers = answers;
    }

    public boolean isOutstanding() {
        return answers == null || answers.size() == 0;
    }

    public void addOption(String code) {
        options.add(new Option(code));
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAnswer(String answer) {
        answers.clear();
        answers.add(answer);
    }

    public Optional<Object> getAnswer() {
        if (answers == null) {
            return Optional.empty();
        }

        if (answers.size() > 1) {
            throw new RuntimeException("more than one answer was found on question " + id);
        }

        if (answers.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(answers.get(0));
    }
}
