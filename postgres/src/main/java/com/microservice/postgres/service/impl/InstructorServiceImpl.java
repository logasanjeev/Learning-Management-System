package com.microservice.postgres.service.impl;

import com.microservice.postgres.dto.request.InstructorRequest;
import com.microservice.postgres.dto.response.InstructorResponse;
import com.microservice.postgres.entity.Instructor;
import com.microservice.postgres.exception.InstructorAlreadyExistsException;
import com.microservice.postgres.exception.InstructorNotFoundException;
import com.microservice.postgres.mapper.InstructorMapper;
import com.microservice.postgres.repository.InstructorRepository;
import com.microservice.postgres.service.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

    @Override
    @Transactional
    public InstructorResponse registerInstructor(InstructorRequest request) {
        log.info("Attempting to register instructor with name: {}", request.getInstructorName());

        Instructor instructor = instructorMapper.toEntity(request);
        Instructor savedInstructor = instructorRepository.save(instructor);
        log.info("Instructor successfully registered with ID: {}", savedInstructor.getInstructorId());
        return instructorMapper.toResponse(savedInstructor);
    }

    @Override
    @Transactional
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
    public InstructorResponse getInstructorByCourseId(Long courseId) {
        log.info("Fetching instructor details for course ID: {}", courseId);

        Instructor instructor = instructorRepository.findByCourseId(courseId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed. Instructor not found for course ID: {}", courseId);
                    return new InstructorNotFoundException("Instructor not found for course ID: " + courseId);
                });

        return instructorMapper.toResponse(instructor);
    }
}
