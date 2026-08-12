package com.microservice.mongodb.service;

import com.microservice.mongodb.dto.request.CourseRequest;
import com.microservice.mongodb.dto.response.CourseDetailsResponse;
import com.microservice.mongodb.dto.response.CourseResponse;

public interface CourseService {
    CourseResponse registerCourse(CourseRequest request);
    void deleteCourse(Long courseId);
    CourseResponse getCourseById(Long courseId);
    CourseDetailsResponse getCourseDetails(Long courseId);
}