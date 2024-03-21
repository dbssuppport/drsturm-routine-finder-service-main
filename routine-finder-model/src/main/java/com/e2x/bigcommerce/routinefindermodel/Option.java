package com.e2x.bigcommerce.routinefindermodel;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Option {
    private String code;
    private String text;
}
