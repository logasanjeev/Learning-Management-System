package com.microservice.postgres.exception;

public class InstructorAlreadyExistsException extends RuntimeException {
    public InstructorAlreadyExistsException(String message) {
        super(message);
    }
}
