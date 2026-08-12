package com.microservice.mongodb.service;

import com.microservice.mongodb.dto.request.EnrollmentRequest;
import com.microservice.mongodb.dto.response.EnrollmentResponse;
import com.microservice.mongodb.dto.response.StudentResponse;
import com.microservice.mongodb.entity.CourseStatus;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enrollStudent(EnrollmentRequest request);
    void withdrawStudent(Long studentId, Long courseId);
    List<EnrollmentResponse> getStudentProgress(Long studentId);
    EnrollmentResponse updateCourseStatus(EnrollmentRequest request);
    long getOrganizationStudentCount();
    long getCourseStudentCount(Long courseId);
    List<StudentResponse> getStudentsByCourseId(Long courseId);
    List<StudentResponse> getStudentsByStatus(CourseStatus status);
    long getEnrolledStudentsCountByCourse(Long courseId);
    long getEnrolledStudentsCountInOrg();
    List<StudentResponse> getStudentsByCourseStatus(CourseStatus status);
}