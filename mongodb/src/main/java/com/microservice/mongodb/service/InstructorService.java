package com.microservice.mongodb.service;

import com.microservice.mongodb.dto.request.InstructorRequest;
import com.microservice.mongodb.dto.response.InstructorResponse;

public interface InstructorService {
    InstructorResponse registerInstructor(InstructorRequest request);
    InstructorResponse updateInstructor(Long instructorId, InstructorRequest request);
    void deleteInstructor(Long instructorId);
    InstructorResponse getInstructorById(Long instructorId);
    long getInstructorCount();
    InstructorResponse getInstructorByCourseId(Long courseId);
}