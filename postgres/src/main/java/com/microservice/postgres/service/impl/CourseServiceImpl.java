package com.microservice.postgres.service.impl;

import com.microservice.postgres.dto.request.CourseRequest;
import com.microservice.postgres.dto.response.CourseDetailsResponse;
import com.microservice.postgres.dto.response.CourseResponse;
import com.microservice.postgres.dto.response.InstructorResponse;
import com.microservice.postgres.dto.response.StudentResponse;
import com.microservice.postgres.entity.Course;
import com.microservice.postgres.entity.Instructor;
import com.microservice.postgres.exception.CourseNotFoundException;
import com.microservice.postgres.exception.InstructorAlreadyAssignedException;
import com.microservice.postgres.exception.InstructorNotFoundException;
import com.microservice.postgres.mapper.CourseMapper;
import com.microservice.postgres.mapper.InstructorMapper;
import com.microservice.postgres.mapper.StudentMapper;
import com.microservice.postgres.repository.CourseRepository;
import com.microservice.postgres.repository.EnrollmentRepository;
import com.microservice.postgres.repository.InstructorRepository;
import com.microservice.postgres.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Override
    @Transactional
    @CacheEvict(value = "instructorByCourse", allEntries = true)
    public CourseResponse registerCourse(CourseRequest request) {
        log.info("Attempting to register course with name: {}", request.getCourseName());

        Course course = courseMapper.toEntity(request);

        if (request.getInstructorId() != null) {
            Long instructorId = request.getInstructorId();

            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> {
                        log.warn("Course registration failed. Instructor not found with ID: {}", instructorId);
                        return new InstructorNotFoundException("Instructor not found with ID: " + instructorId);
                    });

            if (courseRepository.existsByInstructorInstructorId(instructorId)) {
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
    @Caching(evict = {
            @CacheEvict(value = "courses", key = "#courseId"),
            @CacheEvict(value = "courseDetails", key = "#courseId"),
            @CacheEvict(value = "instructorByCourse", key = "#courseId"),
            @CacheEvict(value = "courseStudentCount", key = "#courseId"),
            @CacheEvict(value = "studentsByCourse", key = "#courseId")
    })
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
    @Cacheable(value = "courses", key = "#courseId")
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
    @Cacheable(value = "courseDetails", key = "#courseId")
    public CourseDetailsResponse getCourseDetails(Long courseId) {
        log.info("Fetching complete course details for course ID: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed. Course not found with ID: {}", courseId);
                    return new CourseNotFoundException("Course not found with ID: " + courseId);
                });

        InstructorResponse instructorResponse = instructorRepository.findByCourseId(courseId)
                .map(instructorMapper::toResponse)
                .orElse(null);

        List<StudentResponse> studentResponses = enrollmentRepository.findAllByCourseId(courseId)
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
