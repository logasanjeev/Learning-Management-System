package com.microservice.orchestrator.service;

import com.microservice.orchestrator.client.MongoDbFeignClient;
import com.microservice.orchestrator.client.PostgresFeignClient;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRoutingService {

    private final PostgresFeignClient postgresFeignClient;
    private final MongoDbFeignClient mongoDbFeignClient;

    public CourseResponse registerCourse(DatabaseType db, CourseRequest request) {
        log.info("Routing registerCourse request to database: {}", db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.registerCourse(request);
            case MONGODB -> mongoDbFeignClient.registerCourse(request);
        };
    }

    public void deleteCourse(DatabaseType db, Long courseId) {
        log.info("Routing deleteCourse request for course ID: {} to database: {}", courseId, db);
        switch (db) {
            case POSTGRES -> postgresFeignClient.deleteCourse(courseId);
            case MONGODB -> mongoDbFeignClient.deleteCourse(courseId);
        }
    }

    public InstructorResponse getInstructorByCourseId(DatabaseType db, Long courseId) {
        log.info("Routing getInstructorByCourseId request for course ID: {} to database: {}", courseId, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getInstructorByCourseId(courseId);
            case MONGODB -> mongoDbFeignClient.getInstructorByCourseId(courseId);
        };
    }

    public CourseDetailsResponse getCourseDetailsByCourseId(DatabaseType db, Long courseId) {
        log.info("Routing getCourseDetailsByCourseId request for course ID: {} to database: {}", courseId, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getCourseDetailsByCourseId(courseId);
            case MONGODB -> mongoDbFeignClient.getCourseDetailsByCourseId(courseId);
        };
    }

    public Long getEnrolledStudentsCountByCourse(DatabaseType db, Long courseId) {
        log.info("Routing getEnrolledStudentsCountByCourse request for course ID: {} to database: {}", courseId, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getEnrolledStudentsCountByCourse(courseId);
            case MONGODB -> mongoDbFeignClient.getEnrolledStudentsCountByCourse(courseId);
        };
    }

    public EnrollmentResponse enrollStudent(DatabaseType db, EnrollmentRequest request) {
        log.info("Routing enrollStudent request to database: {}", db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.enrollStudent(request);
            case MONGODB -> mongoDbFeignClient.enrollStudent(request);
        };
    }

    public void withdrawStudent(DatabaseType db, Long studentId, Long courseId) {
        log.info("Routing withdrawStudent request for student ID: {} and course ID: {} to database: {}", studentId, courseId, db);
        switch (db) {
            case POSTGRES -> postgresFeignClient.withdrawStudent(studentId, courseId);
            case MONGODB -> mongoDbFeignClient.withdrawStudent(studentId, courseId);
        }
    }

    public List<EnrollmentResponse> getStudentCourseProgress(DatabaseType db, Long studentId) {
        log.info("Routing getStudentCourseProgress request for student ID: {} to database: {}", studentId, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getStudentCourseProgress(studentId);
            case MONGODB -> mongoDbFeignClient.getStudentCourseProgress(studentId);
        };
    }

    public Long getEnrolledStudentsCountInOrg(DatabaseType db) {
        log.info("Routing getEnrolledStudentsCountInOrg request to database: {}", db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getEnrolledStudentsCountInOrg();
            case MONGODB -> mongoDbFeignClient.getEnrolledStudentsCountInOrg();
        };
    }

    public List<StudentResponse> getStudentsByCourseStatus(DatabaseType db, CourseStatus status) {
        log.info("Routing getStudentsByCourseStatus request with status: {} to database: {}", status, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getStudentsByCourseStatus(status);
            case MONGODB -> mongoDbFeignClient.getStudentsByCourseStatus(status);
        };
    }

    public EnrollmentResponse updateStudentCourseStatus(DatabaseType db, EnrollmentRequest request) {
        log.info("Routing updateStudentCourseStatus request to database: {}", db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.updateStudentCourseStatus(request);
            case MONGODB -> mongoDbFeignClient.updateStudentCourseStatus(request);
        };
    }

    public InstructorResponse registerInstructor(DatabaseType db, InstructorRequest request) {
        log.info("Routing registerInstructor request to database: {}", db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.registerInstructor(request);
            case MONGODB -> mongoDbFeignClient.registerInstructor(request);
        };
    }

    public InstructorResponse updateInstructor(DatabaseType db, Long instructorId, InstructorRequest request) {
        log.info("Routing updateInstructor request for ID: {} to database: {}", instructorId, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.updateInstructor(instructorId, request);
            case MONGODB -> mongoDbFeignClient.updateInstructor(instructorId, request);
        };
    }

    public void deleteInstructor(DatabaseType db, Long instructorId) {
        log.info("Routing deleteInstructor request for ID: {} to database: {}", instructorId, db);
        switch (db) {
            case POSTGRES -> postgresFeignClient.deleteInstructor(instructorId);
            case MONGODB -> mongoDbFeignClient.deleteInstructor(instructorId);
        }
    }

    public Long getInstructorCount(DatabaseType db) {
        log.info("Routing getInstructorCount request to database: {}", db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getInstructorCount();
            case MONGODB -> mongoDbFeignClient.getInstructorCount();
        };
    }

    public StudentResponse registerStudent(DatabaseType db, StudentRequest request) {
        log.info("Routing registerStudent request to database: {}", db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.registerStudent(request);
            case MONGODB -> mongoDbFeignClient.registerStudent(request);
        };
    }

    public StudentResponse updateStudent(DatabaseType db, Long studentId, StudentRequest request) {
        log.info("Routing updateStudent request for ID: {} to database: {}", studentId, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.updateStudent(studentId, request);
            case MONGODB -> mongoDbFeignClient.updateStudent(studentId, request);
        };
    }

    public void deleteStudent(DatabaseType db, Long studentId) {
        log.info("Routing deleteStudent request for ID: {} to database: {}", studentId, db);
        switch (db) {
            case POSTGRES -> postgresFeignClient.deleteStudent(studentId);
            case MONGODB -> mongoDbFeignClient.deleteStudent(studentId);
        }
    }

    public StudentResponse getStudentById(DatabaseType db, Long studentId) {
        log.info("Routing getStudentById request for ID: {} to database: {}", studentId, db);
        return switch (db) {
            case POSTGRES -> postgresFeignClient.getStudentById(studentId);
            case MONGODB -> mongoDbFeignClient.getStudentById(studentId);
        };
    }
}