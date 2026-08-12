package com.microservice.mongodb.exception;

public class InstructorAlreadyAssignedException extends RuntimeException {
    public InstructorAlreadyAssignedException(String message) {
        super(message);
    }
}
