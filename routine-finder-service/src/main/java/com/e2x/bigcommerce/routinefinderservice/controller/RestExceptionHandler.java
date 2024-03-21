package com.e2x.bigcommerce.routinefinderservice.controller;

import com.e2x.bigcommerce.routinefindermodel.ApiError;
import com.e2x.bigcommerce.routinefindermodel.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.HttpMediaTypeNotSupportedException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    private ResponseEntity<ApiError> handleInvalidRequest(HttpMediaTypeNotSupportedException ex) {
        ApiError apiError = new ApiError(ErrorCode.INVALID_REQUEST_MEDIA_TYPE, ex.getLocalizedMessage());

        return toResponse(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ApiError> handleBadRequest(HttpMessageNotReadableException ex) {
        ApiError apiError = new ApiError(ErrorCode.BAD_REQUEST, ex.getLocalizedMessage());

        return toResponse(apiError, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> toResponse(ApiError apiError, HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(apiError, headers, httpStatus);
    }

}
