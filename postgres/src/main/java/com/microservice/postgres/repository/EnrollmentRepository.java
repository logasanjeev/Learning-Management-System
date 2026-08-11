package com.microservice.postgres.repository;

import com.microservice.postgres.entity.CourseStatus;
import com.microservice.postgres.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentStudentIdAndCourseCourseId(Long studentId, Long courseId);

    Optional<Enrollment> findByStudentStudentIdAndCourseCourseId(Long studentId, Long courseId);

    List<Enrollment> findByStudentStudentId(Long studentId);

    List<Enrollment> findByCourseCourseId(Long courseId);

    long countByCourseCourseId(Long courseId);

    @Query("SELECT COUNT(DISTINCT e.student.studentId) FROM Enrollment e")
    long countDistinctStudents();

    @Query("SELECT e FROM Enrollment e WHERE e.course.courseId = :courseId")
    List<Enrollment> findAllByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.courseId = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);

    List<Enrollment> findByStatus(CourseStatus status);
}