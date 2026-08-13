package com.microservice.orchestrator.controller;

import com.microservice.orchestrator.dto.enums.CourseStatus;
import com.microservice.orchestrator.dto.request.CourseRequest;
import com.microservice.orchestrator.dto.request.EnrollmentRequest;
import com.microservice.orchestrator.dto.request.InstructorRequest;
import com.microservice.orchestrator.dto.request.StudentRequest;
import com.microservice.orchestrator.dto.response.CourseDetailsResponse;
import com.microservice.orchestrator.dto.response.CourseResponse;
import com.microservice.orchestrator.dto.response.EnrollmentResponse;
import com.microservice.orchestrator.dto.response.InstructorResponse;
import com.microservice.orchestrator.dto.response.StudentResponse;
import com.microservice.orchestrator.model.DatabaseType;
import com.microservice.orchestrator.service.ApiRoutingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{database}")
@RequiredArgsConstructor
public class ApiOrchestrationController {

    private final ApiRoutingService routingService;

    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> registerCourse(
            @PathVariable DatabaseType database,
            @Valid @RequestBody CourseRequest request) {
        CourseResponse response = routingService.registerCourse(database, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable DatabaseType database,
            @PathVariable Long courseId) {
        routingService.deleteCourse(database, courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/{courseId}/instructor")
    public ResponseEntity<InstructorResponse> getInstructorByCourseId(
            @PathVariable DatabaseType database,
            @PathVariable Long courseId) {
        InstructorResponse response = routingService.getInstructorByCourseId(database, courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{courseId}/details")
    public ResponseEntity<CourseDetailsResponse> getCourseDetailsByCourseId(
            @PathVariable DatabaseType database,
            @PathVariable Long courseId) {
        CourseDetailsResponse response = routingService.getCourseDetailsByCourseId(database, courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{courseId}/students/count")
    public ResponseEntity<Long> getEnrolledStudentsCountByCourse(
            @PathVariable DatabaseType database,
            @PathVariable Long courseId) {
        Long count = routingService.getEnrolledStudentsCountByCourse(database, courseId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/enrollments")
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @PathVariable DatabaseType database,
            @Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = routingService.enrollStudent(database, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/enrollments")
    public ResponseEntity<Void> withdrawStudent(
            @PathVariable DatabaseType database,
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        routingService.withdrawStudent(database, studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/enrollments/students/{studentId}/progress")
    public ResponseEntity<List<EnrollmentResponse>> getStudentCourseProgress(
            @PathVariable DatabaseType database,
            @PathVariable Long studentId) {
        List<EnrollmentResponse> progress = routingService.getStudentCourseProgress(database, studentId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/enrollments/students/count")
    public ResponseEntity<Long> getEnrolledStudentsCountInOrg(
            @PathVariable DatabaseType database) {
        Long count = routingService.getEnrolledStudentsCountInOrg(database);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/enrollments/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourseStatus(
            @PathVariable DatabaseType database,
            @RequestParam CourseStatus status) {
        List<StudentResponse> students = routingService.getStudentsByCourseStatus(database, status);
        return ResponseEntity.ok(students);
    }

    @PatchMapping("/enrollments/status")
    public ResponseEntity<EnrollmentResponse> updateStudentCourseStatus(
            @PathVariable DatabaseType database,
            @Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = routingService.updateStudentCourseStatus(database, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/instructors")
    public ResponseEntity<InstructorResponse> registerInstructor(
            @PathVariable DatabaseType database,
            @Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = routingService.registerInstructor(database, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/instructors/{instructorId}")
    public ResponseEntity<InstructorResponse> updateInstructor(
            @PathVariable DatabaseType database,
            @PathVariable Long instructorId,
            @Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = routingService.updateInstructor(database, instructorId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/instructors/{instructorId}")
    public ResponseEntity<Void> deleteInstructor(
            @PathVariable DatabaseType database,
            @PathVariable Long instructorId) {
        routingService.deleteInstructor(database, instructorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/instructors/count")
    public ResponseEntity<Long> getInstructorCount(
            @PathVariable DatabaseType database) {
        Long count = routingService.getInstructorCount(database);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/students")
    public ResponseEntity<StudentResponse> registerStudent(
            @PathVariable DatabaseType database,
            @Valid @RequestBody StudentRequest request) {
        StudentResponse response = routingService.registerStudent(database, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable DatabaseType database,
            @PathVariable Long studentId,
            @Valid @RequestBody StudentRequest request) {
        StudentResponse response = routingService.updateStudent(database, studentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable DatabaseType database,
            @PathVariable Long studentId) {
        routingService.deleteStudent(database, studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable DatabaseType database,
            @PathVariable Long studentId) {
        StudentResponse response = routingService.getStudentById(database, studentId);
        return ResponseEntity.ok(response);
    }
}