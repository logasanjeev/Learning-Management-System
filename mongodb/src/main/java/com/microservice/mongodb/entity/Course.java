package com.microservice.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    private Long courseId;

    private String courseName;

    private BigDecimal courseFee;

    @DocumentReference(lazy = true)
    private Instructor instructor;

    @DocumentReference(lazy = true)
    private List<Enrollment> enrollments = new ArrayList<>();
}