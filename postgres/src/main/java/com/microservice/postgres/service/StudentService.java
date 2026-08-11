package com.microservice.postgres.service;

import com.microservice.postgres.dto.request.StudentRequest;
import com.microservice.postgres.dto.response.StudentResponse;

public interface StudentService {
    StudentResponse registerStudent(StudentRequest request);
    StudentResponse updateStudent(Long studentId, StudentRequest request);
    void deleteStudent(Long studentId);
    StudentResponse getStudentById(Long studentId);
}
