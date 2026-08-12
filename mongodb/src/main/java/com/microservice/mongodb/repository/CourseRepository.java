package com.microservice.mongodb.repository;

import com.microservice.mongodb.entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, Long> {
    boolean existsByInstructor_InstructorId(Long instructorId);
    Optional<Course> findByInstructor_InstructorId(Long instructorId);
}