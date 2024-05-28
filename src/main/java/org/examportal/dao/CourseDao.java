package org.examportal.dao;

import org.examportal.entities.Course;

import java.util.List;

public interface CourseDao {
    void createCourse(Course course);
    Course getCourseById(Long id);
    List<Course> getAllCourses();
    void updateCourse(Course course);
    void deleteCourse(Long id);
}
