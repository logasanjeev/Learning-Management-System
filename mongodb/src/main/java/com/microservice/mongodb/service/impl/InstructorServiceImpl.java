package com.microservice.mongodb.service.impl;

import com.microservice.mongodb.dto.request.InstructorRequest;
import com.microservice.mongodb.dto.response.InstructorResponse;
import com.microservice.mongodb.entity.Course;
import com.microservice.mongodb.entity.Instructor;
import com.microservice.mongodb.exception.InstructorNotFoundException;
import com.microservice.mongodb.mapper.InstructorMapper;
import com.microservice.mongodb.repository.CourseRepository;
import com.microservice.mongodb.repository.InstructorRepository;
import com.microservice.mongodb.service.InstructorService;
import com.microservice.mongodb.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final InstructorMapper instructorMapper;
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    @Transactional
    public InstructorResponse registerInstructor(InstructorRequest request) {
        log.info("Attempting to register instructor with name: {}", request.getInstructorName());

        Instructor instructor = instructorMapper.toEntity(request);
        instructor.setInstructorId(sequenceGenerator.generateSequence("instructors_sequence"));

        Instructor savedInstructor = instructorRepository.save(instructor);
        log.info("Instructor successfully registered with ID: {}", savedInstructor.getInstructorId());
        return instructorMapper.toResponse(savedInstructor);
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(value = "instructors", key = "#instructorId"),
            evict = @CacheEvict(value = "instructorsByCourse", allEntries = true)
    )
    public InstructorResponse updateInstructor(Long instructorId, InstructorRequest request) {
        log.info("Attempting to update instructor details for ID: {}", instructorId);

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> {
                    log.warn("Update failed. Instructor not found with ID: {}", instructorId);
                    return new InstructorNotFoundException("Instructor not found with ID: " + instructorId);
                });

        instructorMapper.updateEntity(request, instructor);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        log.info("Instructor details successfully updated for ID: {}", updatedInstructor.getInstructorId());
        return instructorMapper.toResponse(updatedInstructor);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "instructors", key = "#instructorId"),
            @CacheEvict(value = "instructorsByCourse", allEntries = true)
    })
    public void deleteInstructor(Long instructorId) {
        log.info("Attempting to delete instructor with ID: {}", instructorId);

        if (!instructorRepository.existsById(instructorId)) {
            log.warn("Delete failed. Instructor not found with ID: {}", instructorId);
            throw new InstructorNotFoundException("Instructor not found with ID: " + instructorId);
        }

        instructorRepository.deleteById(instructorId);
        log.info("Instructor successfully deleted with ID: {}", instructorId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "instructors", key = "#instructorId")
    public InstructorResponse getInstructorById(Long instructorId) {
        log.info("Fetching instructor details for ID: {}", instructorId);

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed. Instructor not found with ID: {}", instructorId);
                    return new InstructorNotFoundException("Instructor not found with ID: " + instructorId);
                });

        return instructorMapper.toResponse(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public long getInstructorCount() {
        log.info("Counting total instructors in organization");
        return instructorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "instructorsByCourse", key = "#courseId")
    public InstructorResponse getInstructorByCourseId(Long courseId) {
        log.info("Fetching instructor details for course ID: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new InstructorNotFoundException("Course not found with ID: " + courseId));

        if (course.getInstructor() == null) {
            throw new InstructorNotFoundException("No instructor assigned to course ID: " + courseId);
        }

        return instructorMapper.toResponse(course.getInstructor());
    }
}