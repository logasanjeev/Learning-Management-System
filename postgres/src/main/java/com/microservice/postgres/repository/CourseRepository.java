package com.microservice.postgres.repository;

import com.microservice.postgres.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByInstructorInstructorId(Long instructorId);
}
