package com.microservice.orchestrator.client;

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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "mongodb-service", url = "${services.mongodb.url}")
public interface MongoDbFeignClient {

    @PostMapping("/api/v1/courses")
    CourseResponse registerCourse(@RequestBody CourseRequest request);

    @DeleteMapping("/api/v1/courses/{courseId}")
    void deleteCourse(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/v1/courses/{courseId}/instructor")
    InstructorResponse getInstructorByCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/v1/courses/{courseId}/details")
    CourseDetailsResponse getCourseDetailsByCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/v1/courses/{courseId}/students/count")
    Long getEnrolledStudentsCountByCourse(@PathVariable("courseId") Long courseId);

    @PostMapping("/api/v1/enrollments")
    EnrollmentResponse enrollStudent(@RequestBody EnrollmentRequest request);

    @DeleteMapping("/api/v1/enrollments")
    void withdrawStudent(@RequestParam("studentId") Long studentId, @RequestParam("courseId") Long courseId);

    @GetMapping("/api/v1/enrollments/students/{studentId}/progress")
    List<EnrollmentResponse> getStudentCourseProgress(@PathVariable("studentId") Long studentId);

    @GetMapping("/api/v1/enrollments/students/count")
    Long getEnrolledStudentsCountInOrg();

    @GetMapping("/api/v1/enrollments/students")
    List<StudentResponse> getStudentsByCourseStatus(@RequestParam("status") CourseStatus status);

    @PatchMapping("/api/v1/enrollments/status")
    EnrollmentResponse updateStudentCourseStatus(@RequestBody EnrollmentRequest request);

    @PostMapping("/api/v1/instructors")
    InstructorResponse registerInstructor(@RequestBody InstructorRequest request);

    @PutMapping("/api/v1/instructors/{instructorId}")
    InstructorResponse updateInstructor(@PathVariable("instructorId") Long instructorId, @RequestBody InstructorRequest request);

    @DeleteMapping("/api/v1/instructors/{instructorId}")
    void deleteInstructor(@PathVariable("instructorId") Long instructorId);

    @GetMapping("/api/v1/instructors/count")
    Long getInstructorCount();

    @PostMapping("/api/v1/students")
    StudentResponse registerStudent(@RequestBody StudentRequest request);

    @PutMapping("/api/v1/students/{studentId}")
    StudentResponse updateStudent(@PathVariable("studentId") Long studentId, @RequestBody StudentRequest request);

    @DeleteMapping("/api/v1/students/{studentId}")
    void deleteStudent(@PathVariable("studentId") Long studentId);

    @GetMapping("/api/v1/students/{studentId}")
    StudentResponse getStudentById(@PathVariable("studentId") Long studentId);
}