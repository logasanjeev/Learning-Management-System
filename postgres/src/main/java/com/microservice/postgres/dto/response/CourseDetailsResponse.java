package com.microservice.postgres.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailsResponse {
    private Long courseId;
    private String courseName;
    private BigDecimal courseFee;
    private InstructorResponse instructor;
    private List<StudentResponse> students;
}
