package com.microservice.mongodb.mapper;

import com.microservice.mongodb.dto.request.StudentRequest;
import com.microservice.mongodb.dto.response.StudentResponse;
import com.microservice.mongodb.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequest request) {
        if (request == null) return null;

        Student student = new Student();
        student.setStudentName(request.getStudentName());
        student.setStudentDob(request.getStudentDob());
        return student;
    }

    public StudentResponse toResponse(Student student) {
        if (student == null) return null;

        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .studentName(student.getStudentName())
                .studentDob(student.getStudentDob())
                .build();
    }

    public void updateEntity(StudentRequest request, Student student) {
        if (request == null || student == null) return;

        if (request.getStudentName() != null) {
            student.setStudentName(request.getStudentName());
        }
        if (request.getStudentDob() != null) {
            student.setStudentDob(request.getStudentDob());
        }
    }
}