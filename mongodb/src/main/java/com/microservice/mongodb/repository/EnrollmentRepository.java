package com.microservice.mongodb.repository;

import com.microservice.mongodb.entity.CourseStatus;
import com.microservice.mongodb.entity.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, Long> {
    boolean existsByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    Optional<Enrollment> findByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    List<Enrollment> findByStudent_StudentId(Long studentId);
    List<Enrollment> findByCourse_CourseId(Long courseId);
    long countByCourse_CourseId(Long courseId);
    List<Enrollment> findByStatus(CourseStatus status);
}