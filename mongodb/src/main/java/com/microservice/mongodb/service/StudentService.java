package com.microservice.mongodb.service;

import com.microservice.mongodb.dto.request.StudentRequest;
import com.microservice.mongodb.dto.response.StudentResponse;

public interface StudentService {
    StudentResponse registerStudent(StudentRequest request);
    StudentResponse updateStudent(Long studentId, StudentRequest request);
    void deleteStudent(Long studentId);
    StudentResponse getStudentById(Long studentId);
}