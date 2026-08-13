package com.microservice.orchestrator.exception;

import com.microservice.orchestrator.dto.response.ErrorResponse;
import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {

    private final int status;
    private final ErrorResponse errorResponse;

    public CustomFeignException(int status, ErrorResponse errorResponse) {
        super(errorResponse != null ? errorResponse.getMessage() : "Downstream service error");
        this.status = status;
        this.errorResponse = errorResponse;
    }
}