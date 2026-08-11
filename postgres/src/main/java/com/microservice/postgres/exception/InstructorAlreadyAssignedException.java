package com.microservice.postgres.exception;

public class InstructorAlreadyAssignedException extends RuntimeException {
    public InstructorAlreadyAssignedException(String message) {
        super(message);
    }
}
