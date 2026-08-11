package com.microservice.postgres.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long studentId;
    private String studentName;
    private LocalDate studentDob;
}
