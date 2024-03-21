package com.e2x.bigcommerce.routinefindermodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashSet;

@Data
@AllArgsConstructor
public class Routine {
    private String id;
    private LinkedHashSet<Step> steps;

    public Routine() { }
}
