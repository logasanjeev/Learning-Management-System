package com.microservice.postgres.controller;

import com.microservice.postgres.dto.request.CourseRequest;
import com.microservice.postgres.dto.response.CourseDetailsResponse;
import com.microservice.postgres.dto.response.CourseResponse;
import com.microservice.postgres.dto.response.InstructorResponse;
import com.microservice.postgres.service.CourseService;
import com.microservice.postgres.service.EnrollmentService;
import com.microservice.postgres.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final InstructorService instructorService;
    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<CourseResponse> registerCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.registerCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/instructor")
    public ResponseEntity<InstructorResponse> getInstructorByCourseId(@PathVariable Long courseId) {
        InstructorResponse response = instructorService.getInstructorByCourseId(courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/details")
    public ResponseEntity<CourseDetailsResponse> getCourseDetailsByCourseId(@PathVariable Long courseId) {
        CourseDetailsResponse response = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/students/count")
    public ResponseEntity<Long> getEnrolledStudentsCountByCourse(@PathVariable Long courseId) {
        long count = enrollmentService.getEnrolledStudentsCountByCourse(courseId);
        return ResponseEntity.ok(count);
    }
}
