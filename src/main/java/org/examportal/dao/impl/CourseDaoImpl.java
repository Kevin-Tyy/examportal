package org.examportal.dao.impl;

import org.examportal.dao.CourseDao;
import org.examportal.entities.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CourseDaoImpl implements CourseDao {

    private final SessionFactory sessionFactory;

    public CourseDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void createCourse(Course course) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(course);
        transaction.commit();
        session.close();
    }

    @Override
    public Course getCourseById(Long id) {
        Session session = sessionFactory.openSession();
        Course course = session.get(Course.class, id);
        session.close();
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        Session session = sessionFactory.openSession();
        List<Course> courses = session.createQuery("from Course", Course.class).list();
        session.close();
        return courses;
    }

    @Override
    public void updateCourse(Course course) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(course);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteCourse(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Course course = session.get(Course.class, id);
        if (course != null) {
            session.delete(course);
        }
        transaction.commit();
        session.close();
    }
}
