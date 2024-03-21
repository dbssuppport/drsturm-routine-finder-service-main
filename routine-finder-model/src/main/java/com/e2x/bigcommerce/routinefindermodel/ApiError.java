package com.e2x.bigcommerce.routinefindermodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiError extends Error {
    private final Map<String, Error> errors = new HashMap<>();

    public ApiError(String code) {
        this(code, null);
    }

    public ApiError(String code, String message) {
        super(code, message, null);
    }

    public Error addError(String key, String code, String message) {
        Error error = new Error(code, message, null);
        errors.put(key, error);

        return error;
    }
}
