package com.e2x.bigcommerce.routinefindermodel;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RoutineEnquiry {
    private boolean complete;

    private List<Question> questions = Lists.newArrayList();
    private List<Routine> routines = Lists.newArrayList();
}

