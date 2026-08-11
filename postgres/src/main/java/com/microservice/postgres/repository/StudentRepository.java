package com.microservice.postgres.repository;

import com.microservice.postgres.entity.CourseStatus;
import com.microservice.postgres.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findDistinctByEnrollmentsStatus(CourseStatus status);
}
