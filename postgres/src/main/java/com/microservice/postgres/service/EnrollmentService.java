package com.microservice.postgres.service;

import com.microservice.postgres.dto.request.EnrollmentRequest;
import com.microservice.postgres.dto.response.EnrollmentResponse;
import com.microservice.postgres.dto.response.StudentResponse;
import com.microservice.postgres.entity.CourseStatus;

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
