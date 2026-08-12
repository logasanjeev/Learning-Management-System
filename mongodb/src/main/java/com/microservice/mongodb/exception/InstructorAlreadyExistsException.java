package com.microservice.mongodb.exception;

public class InstructorAlreadyExistsException extends RuntimeException {
    public InstructorAlreadyExistsException(String message) {
        super(message);
    }
}
