package com.microservice.postgres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "instructor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Long instructorId;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @Column(name = "instructor_dob", nullable = false)
    private LocalDate instructorDob;

    @OneToOne(mappedBy = "instructor")
    private Course course;
}
