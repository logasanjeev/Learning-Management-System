package com.microservice.postgres.mapper;

import com.microservice.postgres.dto.request.CourseRequest;
import com.microservice.postgres.dto.response.CourseResponse;
import com.microservice.postgres.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    private final InstructorMapper instructorMapper;

    public CourseMapper(InstructorMapper instructorMapper) {
        this.instructorMapper = instructorMapper;
    }

    public Course toEntity(CourseRequest request) {
        if (request == null) return null;

        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setCourseFee(request.getCourseFee());
        return course;
    }

    public CourseResponse toResponse(Course course) {
        if (course == null) return null;

        return CourseResponse.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .courseFee(course.getCourseFee())
                .instructor(instructorMapper.toResponse(course.getInstructor()))
                .build();
    }

    public void updateEntity(CourseRequest request, Course course) {
        if (request == null || course == null) return;

        if (request.getCourseName() != null) {
            course.setCourseName(request.getCourseName());
        }
        if (request.getCourseFee() != null) {
            course.setCourseFee(request.getCourseFee());
        }
    }
}
