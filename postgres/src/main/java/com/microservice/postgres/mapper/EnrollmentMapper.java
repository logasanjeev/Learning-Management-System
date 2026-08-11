package com.microservice.postgres.mapper;

import com.microservice.postgres.dto.request.EnrollmentRequest;
import com.microservice.postgres.dto.response.EnrollmentResponse;
import com.microservice.postgres.entity.Course;
import com.microservice.postgres.entity.Enrollment;
import com.microservice.postgres.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public Enrollment toEntity(EnrollmentRequest request) {
        if (request == null) return null;

        Enrollment enrollment = new Enrollment();
        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }
        return enrollment;
    }

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        if (enrollment == null) return null;

        Student student = enrollment.getStudent();
        Course course = enrollment.getCourse();

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(student != null ? student.getStudentId() : null)
                .studentName(student != null ? student.getStudentName() : null)
                .courseId(course != null ? course.getCourseId() : null)
                .courseName(course != null ? course.getCourseName() : null)
                .status(enrollment.getStatus())
                .build();
    }

    public void updateEntity(EnrollmentRequest request, Enrollment enrollment) {
        if (request == null || enrollment == null) return;

        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }
    }
}
