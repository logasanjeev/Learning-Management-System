package com.microservice.postgres.controller;

import com.microservice.postgres.dto.request.EnrollmentRequest;
import com.microservice.postgres.dto.response.EnrollmentResponse;
import com.microservice.postgres.dto.response.StudentResponse;
import com.microservice.postgres.entity.CourseStatus;
import com.microservice.postgres.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enrollStudent(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdrawStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        enrollmentService.withdrawStudent(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students/{studentId}/progress")
    public ResponseEntity<List<EnrollmentResponse>> getStudentCourseProgress(@PathVariable Long studentId) {
        List<EnrollmentResponse> progress = enrollmentService.getStudentProgress(studentId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/students/count")
    public ResponseEntity<Long> getEnrolledStudentsCountInOrg() {
        long count = enrollmentService.getEnrolledStudentsCountInOrg();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourseStatus(@RequestParam CourseStatus status) {
        List<StudentResponse> students = enrollmentService.getStudentsByCourseStatus(status);
        return ResponseEntity.ok(students);
    }

    @PatchMapping("/status")
    public ResponseEntity<EnrollmentResponse> updateStudentCourseStatus(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.updateCourseStatus(request);
        return ResponseEntity.ok(response);
    }
}
