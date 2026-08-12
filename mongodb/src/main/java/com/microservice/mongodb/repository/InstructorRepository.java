package com.microservice.mongodb.repository;

import com.microservice.mongodb.entity.Instructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends MongoRepository<Instructor, Long> {
}