package com.microservice.mongodb.service.impl;

import com.microservice.mongodb.dto.request.EnrollmentRequest;
import com.microservice.mongodb.dto.response.EnrollmentResponse;
import com.microservice.mongodb.dto.response.StudentResponse;
import com.microservice.mongodb.entity.Course;
import com.microservice.mongodb.entity.CourseStatus;
import com.microservice.mongodb.entity.Enrollment;
import com.microservice.mongodb.entity.Student;
import com.microservice.mongodb.exception.*;
import com.microservice.mongodb.mapper.EnrollmentMapper;
import com.microservice.mongodb.mapper.StudentMapper;
import com.microservice.mongodb.repository.CourseRepository;
import com.microservice.mongodb.repository.EnrollmentRepository;
import com.microservice.mongodb.repository.StudentRepository;
import com.microservice.mongodb.service.EnrollmentService;
import com.microservice.mongodb.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final StudentMapper studentMapper;
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    @Transactional
    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {
        Long studentId = request.getStudentId();
        Long courseId = request.getCourseId();

        log.info("Attempting to enroll student ID: {} into course ID: {}", studentId, courseId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Enrollment failed. Student not found with ID: {}", studentId);
                    return new StudentNotFoundException("Student not found with ID: " + studentId);
                });

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Enrollment failed. Course not found with ID: {}", courseId);
                    return new CourseNotFoundException("Course not found with ID: " + courseId);
                });

        if (enrollmentRepository.existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
            log.warn("Enrollment failed. Student ID: {} is already enrolled in course ID: {}", studentId, courseId);
            throw new EnrollmentAlreadyExistsException("Student is already enrolled in course ID: " + courseId);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setId(sequenceGenerator.generateSequence("enrollments_sequence"));
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(request.getStatus() != null ? request.getStatus() : CourseStatus.TO_DO);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Successfully enrolled student ID: {} into course ID: {}", studentId, courseId);
        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Override
    @Transactional
    public void withdrawStudent(Long studentId, Long courseId) {
        log.info("Attempting to withdraw student ID: {} from course ID: {}", studentId, courseId);

        Enrollment enrollment = enrollmentRepository.findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> {
                    log.warn("Withdrawal failed. No enrollment found for student ID: {} in course ID: {}", studentId, courseId);
                    return new EnrollmentNotFoundException("Enrollment record not found for student ID: " + studentId + " in course ID: " + courseId);
                });

        enrollmentRepository.delete(enrollment);
        log.info("Successfully withdrew student ID: {} from course ID: {}", studentId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentProgress(Long studentId) {
        log.info("Fetching progress for student ID: {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            log.warn("Progress fetch failed. Student not found with ID: {}", studentId);
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }

        return enrollmentRepository.findByStudent_StudentId(studentId).stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentResponse updateCourseStatus(EnrollmentRequest request) {
        Long studentId = request.getStudentId();
        Long courseId = request.getCourseId();
        CourseStatus targetStatus = request.getStatus();

        log.info("Attempting status update for student ID: {} in course ID: {} to status: {}", studentId, courseId, targetStatus);

        Enrollment enrollment = enrollmentRepository.findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> {
                    log.warn("Status update failed. Enrollment not found for student ID: {} in course ID: {}", studentId, courseId);
                    return new EnrollmentNotFoundException("Enrollment record not found for student ID: " + studentId + " in course ID: " + courseId);
                });

        CourseStatus currentStatus = enrollment.getStatus();

        if (!isValidStatusTransition(currentStatus, targetStatus)) {
            log.warn("Invalid status transition attempted from {} to {} for student ID: {}", currentStatus, targetStatus, studentId);
            throw new InvalidStatusTransitionException("Invalid status transition from " + currentStatus + " to " + targetStatus);
        }

        enrollment.setStatus(targetStatus);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Status successfully updated to {} for student ID: {} in course ID: {}", targetStatus, studentId, courseId);
        return enrollmentMapper.toResponse(updatedEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public long getOrganizationStudentCount() {
        log.info("Counting unique enrolled students across organization");
        return enrollmentRepository.findAll().stream()
                .map(e -> e.getStudent() != null ? e.getStudent().getStudentId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getCourseStudentCount(Long courseId) {
        log.info("Counting enrolled students for course ID: {}", courseId);

        if (!courseRepository.existsById(courseId)) {
            log.warn("Count failed. Course not found with ID: {}", courseId);
            throw new CourseNotFoundException("Course not found with ID: " + courseId);
        }

        return enrollmentRepository.countByCourse_CourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByCourseId(Long courseId) {
        log.info("Fetching enrolled student details for course ID: {}", courseId);

        if (!courseRepository.existsById(courseId)) {
            log.warn("Fetch failed. Course not found with ID: {}", courseId);
            throw new CourseNotFoundException("Course not found with ID: " + courseId);
        }

        return enrollmentRepository.findByCourse_CourseId(courseId).stream()
                .map(Enrollment::getStudent)
                .filter(Objects::nonNull)
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByStatus(CourseStatus status) {
        log.info("Fetching all students filtered by course status: {}", status);

        return enrollmentRepository.findByStatus(status).stream()
                .map(Enrollment::getStudent)
                .filter(Objects::nonNull)
                .distinct()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    private boolean isValidStatusTransition(CourseStatus currentStatus, CourseStatus targetStatus) {
        if (currentStatus == null || targetStatus == null || currentStatus == targetStatus) {
            return false;
        }

        return (currentStatus == CourseStatus.TO_DO && (targetStatus == CourseStatus.IN_PROGRESS || targetStatus == CourseStatus.COMPLETED))
                || (currentStatus == CourseStatus.IN_PROGRESS && targetStatus == CourseStatus.COMPLETED);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEnrolledStudentsCountByCourse(Long courseId) {
        log.info("Counting enrolled students for course ID: {}", courseId);
        return enrollmentRepository.countByCourse_CourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEnrolledStudentsCountInOrg() {
        return getOrganizationStudentCount();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByCourseStatus(CourseStatus status) {
        return getStudentsByStatus(status);
    }
}