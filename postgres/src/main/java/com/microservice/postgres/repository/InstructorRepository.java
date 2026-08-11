package com.microservice.postgres.repository;

import com.microservice.postgres.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    @Query("SELECT i FROM Instructor i WHERE i.course.courseId = :courseId")
    Optional<Instructor> findByCourseId(@Param("courseId") Long courseId);
}
