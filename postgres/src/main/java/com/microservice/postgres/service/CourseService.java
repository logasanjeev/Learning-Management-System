package com.microservice.postgres.service;

import com.microservice.postgres.dto.request.CourseRequest;
import com.microservice.postgres.dto.response.CourseDetailsResponse;
import com.microservice.postgres.dto.response.CourseResponse;

public interface CourseService {
    CourseResponse registerCourse(CourseRequest request);
    void deleteCourse(Long courseId);
    CourseResponse getCourseById(Long courseId);
    CourseDetailsResponse getCourseDetails(Long courseId);
}
