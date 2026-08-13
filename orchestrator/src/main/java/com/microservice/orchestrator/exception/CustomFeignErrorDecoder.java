package com.microservice.orchestrator.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.orchestrator.dto.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.time.LocalDateTime;

@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() != null) {
            try (InputStream bodyIs = response.body().asInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());

                ErrorResponse errorResponse = mapper.readValue(bodyIs, ErrorResponse.class);
                log.warn("Extracted downstream error: status={}, message={}", response.status(), errorResponse.getMessage());
                return new CustomFeignException(response.status(), errorResponse);
            } catch (Exception e) {
                log.error("Failed to parse error body from downstream service", e);
            }
        }

        ErrorResponse fallbackError = ErrorResponse.builder()
                .status(response.status())
                .error("Downstream Error")
                .message("Error occurred in downstream service call")
                .timestamp(LocalDateTime.now())
                .build();

        return new CustomFeignException(response.status(), fallbackError);
    }
}