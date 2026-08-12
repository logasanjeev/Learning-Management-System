package com.microservice.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "enrollments")
@CompoundIndex(name = "uk_enrollment_student_course", def = "{'student': 1, 'course': 1}", unique = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    private Long id;

    @DocumentReference
    private Student student;

    @DocumentReference
    private Course course;

    private CourseStatus status = CourseStatus.TO_DO;
}