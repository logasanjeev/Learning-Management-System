package com.microservice.mongodb.mapper;

import com.microservice.mongodb.dto.request.InstructorRequest;
import com.microservice.mongodb.dto.response.InstructorResponse;
import com.microservice.mongodb.entity.Instructor;
import org.springframework.stereotype.Component;

@Component
public class InstructorMapper {

    public Instructor toEntity(InstructorRequest request) {
        if (request == null) return null;

        Instructor instructor = new Instructor();
        instructor.setInstructorName(request.getInstructorName());
        instructor.setInstructorDob(request.getInstructorDob());
        return instructor;
    }

    public InstructorResponse toResponse(Instructor instructor) {
        if (instructor == null) return null;

        return InstructorResponse.builder()
                .instructorId(instructor.getInstructorId())
                .instructorName(instructor.getInstructorName())
                .instructorDob(instructor.getInstructorDob())
                .build();
    }

    public void updateEntity(InstructorRequest request, Instructor instructor) {
        if (request == null || instructor == null) return;

        if (request.getInstructorName() != null) {
            instructor.setInstructorName(request.getInstructorName());
        }
        if (request.getInstructorDob() != null) {
            instructor.setInstructorDob(request.getInstructorDob());
        }
    }
}