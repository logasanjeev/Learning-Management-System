package com.microservice.postgres.service;

import com.microservice.postgres.dto.request.InstructorRequest;
import com.microservice.postgres.dto.response.InstructorResponse;

public interface InstructorService {
    InstructorResponse registerInstructor(InstructorRequest request);
    InstructorResponse updateInstructor(Long instructorId, InstructorRequest request);
    void deleteInstructor(Long instructorId);
    InstructorResponse getInstructorById(Long instructorId);
    long getInstructorCount();
    InstructorResponse getInstructorByCourseId(Long courseId);
}
