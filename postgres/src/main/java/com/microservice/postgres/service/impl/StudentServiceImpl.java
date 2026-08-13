package com.microservice.postgres.service.impl;

import com.microservice.postgres.dto.request.StudentRequest;
import com.microservice.postgres.dto.response.StudentResponse;
import com.microservice.postgres.entity.Student;
import com.microservice.postgres.exception.StudentNotFoundException;
import com.microservice.postgres.mapper.StudentMapper;
import com.microservice.postgres.repository.StudentRepository;
import com.microservice.postgres.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponse registerStudent(StudentRequest request) {
        log.info("Attempting to register student with name: {}", request.getStudentName());

        Student student = studentMapper.toEntity(request);
        Student savedStudent = studentRepository.save(student);
        log.info("Student successfully registered with ID: {}", savedStudent.getStudentId());
        return studentMapper.toResponse(savedStudent);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "students", key = "#studentId"),
            @CacheEvict(value = "studentProgress", key = "#studentId"),
            @CacheEvict(value = "studentsByCourse", allEntries = true),
            @CacheEvict(value = "studentsByStatus", allEntries = true)
    })
    public StudentResponse updateStudent(Long studentId, StudentRequest request) {
        log.info("Attempting to update student details for ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Update failed. Student not found with ID: {}", studentId);
                    return new StudentNotFoundException("Student not found with ID: " + studentId);
                });

        studentMapper.updateEntity(request, student);
        Student updatedStudent = studentRepository.save(student);
        log.info("Student details successfully updated for ID: {}", updatedStudent.getStudentId());
        return studentMapper.toResponse(updatedStudent);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "students", key = "#studentId"),
            @CacheEvict(value = "studentProgress", key = "#studentId"),
            @CacheEvict(value = "studentsByCourse", allEntries = true),
            @CacheEvict(value = "studentsByStatus", allEntries = true)
    })
    public void deleteStudent(Long studentId) {
        log.info("Attempting to delete student with ID: {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            log.warn("Delete failed. Student not found with ID: {}", studentId);
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }

        studentRepository.deleteById(studentId);
        log.info("Student successfully deleted with ID: {}", studentId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "students", key = "#studentId")
    public StudentResponse getStudentById(Long studentId) {
        log.info("Fetching student details for ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed. Student not found with ID: {}", studentId);
                    return new StudentNotFoundException("Student not found with ID: " + studentId);
                });

        return studentMapper.toResponse(student);
    }
}
