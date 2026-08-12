package com.microservice.mongodb.service.impl;

import com.microservice.mongodb.dto.request.CourseRequest;
import com.microservice.mongodb.dto.response.CourseDetailsResponse;
import com.microservice.mongodb.dto.response.CourseResponse;
import com.microservice.mongodb.dto.response.InstructorResponse;
import com.microservice.mongodb.dto.response.StudentResponse;
import com.microservice.mongodb.entity.Course;
import com.microservice.mongodb.entity.Instructor;
import com.microservice.mongodb.exception.CourseNotFoundException;
import com.microservice.mongodb.exception.InstructorAlreadyAssignedException;
import com.microservice.mongodb.exception.InstructorNotFoundException;
import com.microservice.mongodb.mapper.CourseMapper;
import com.microservice.mongodb.mapper.InstructorMapper;
import com.microservice.mongodb.mapper.StudentMapper;
import com.microservice.mongodb.repository.CourseRepository;
import com.microservice.mongodb.repository.EnrollmentRepository;
import com.microservice.mongodb.repository.InstructorRepository;
import com.microservice.mongodb.service.CourseService;
import com.microservice.mongodb.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final CourseMapper courseMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final InstructorMapper instructorMapper;
    private final StudentMapper studentMapper;
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    @Transactional
    public CourseResponse registerCourse(CourseRequest request) {
        log.info("Attempting to register course with name: {}", request.getCourseName());

        Course course = courseMapper.toEntity(request);
        course.setCourseId(sequenceGenerator.generateSequence("courses_sequence"));

        if (request.getInstructorId() != null) {
            Long instructorId = request.getInstructorId();

            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> {
                        log.warn("Course registration failed. Instructor not found with ID: {}", instructorId);
                        return new InstructorNotFoundException("Instructor not found with ID: " + instructorId);
                    });

            if (courseRepository.existsByInstructor_InstructorId(instructorId)) {
                log.warn("Course registration failed. Instructor ID {} is already assigned to another course", instructorId);
                throw new InstructorAlreadyAssignedException("Instructor ID " + instructorId + " is already assigned to a course");
            }

            course.setInstructor(instructor);
        }

        Course savedCourse = courseRepository.save(course);
        log.info("Course successfully registered with ID: {}", savedCourse.getCourseId());
        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        log.info("Attempting to delete course with ID: {}", courseId);

        if (!courseRepository.existsById(courseId)) {
            log.warn("Delete failed. Course not found with ID: {}", courseId);
            throw new CourseNotFoundException("Course not found with ID: " + courseId);
        }

        courseRepository.deleteById(courseId);
        log.info("Course successfully deleted with ID: {}", courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long courseId) {
        log.info("Fetching course details for ID: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed. Course not found with ID: {}", courseId);
                    return new CourseNotFoundException("Course not found with ID: " + courseId);
                });

        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailsResponse getCourseDetails(Long courseId) {
        log.info("Fetching complete course details for course ID: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed. Course not found with ID: {}", courseId);
                    return new CourseNotFoundException("Course not found with ID: " + courseId);
                });

        InstructorResponse instructorResponse = (course.getInstructor() != null)
                ? instructorMapper.toResponse(course.getInstructor())
                : null;

        List<StudentResponse> studentResponses = enrollmentRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(enrollment -> studentMapper.toResponse(enrollment.getStudent()))
                .toList();

        return CourseDetailsResponse.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .courseFee(course.getCourseFee())
                .instructor(instructorResponse)
                .students(studentResponses)
                .build();
    }
}