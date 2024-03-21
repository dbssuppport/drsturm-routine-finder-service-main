package com.e2x.bigcommerce.routinefinder.enquiry;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Step {
    private final Integer sequence;
    private final String skuId;

    public Step(Integer sequence, String skuId) {
        this.sequence = sequence;
        this.skuId = skuId;
    }
}
