package com.microservice.postgres.mapper;

import com.microservice.postgres.dto.request.InstructorRequest;
import com.microservice.postgres.dto.response.InstructorResponse;
import com.microservice.postgres.entity.Instructor;
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
