package org.examportal.dao.impl;

import jakarta.persistence.TypedQuery;
import org.examportal.dao.ExamDao;
import org.examportal.entities.Exam;
import org.examportal.entities.Result;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ExamDaoImpl implements ExamDao {

    private final SessionFactory sessionFactory;

    public ExamDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Exam getExamById(Long examId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Exam.class, examId);
        }
    }

    @Override
    public List<Exam> getAllExams() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Exam", Exam.class).list();
        }
    }

    @Override
    public void createExam(Exam exam) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(exam);
            transaction.commit();
        }
    }

    @Override
    public List<Exam> getExamsByCreator(Long creatorId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Exam> query = session.createQuery("from Exam where creator.id = :creatorId", Exam.class);
            query.setParameter("creatorId", creatorId);
            return query.list();
        }
    }

    @Override
    public List<Result> getExamResults(long examId) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Result> query = session.createQuery("SELECT r FROM Result r WHERE r.exam.id = :examId", Result.class);
            query.setParameter("examId", examId);
            return query.getResultList();
        }
    }

    @Override
    public List<Result> getUserExamResults(long userId) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Result> query = session.createQuery("SELECT r FROM Result r WHERE r.student.id = :userId", Result.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
    }

    @Override
    public void saveExamResult(Result result) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(result);
            transaction.commit();
        }
    }

    @Override
    public boolean hasUserAttemptedExam(long userId, long examId) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Long> query = session.createQuery("SELECT COUNT(r) FROM Result r WHERE r.student.id = :userId AND r.exam.id = :examId", Long.class);
            query.setParameter("userId", userId);
            query.setParameter("examId", examId);
            Long count = query.getSingleResult();
            return count > 0;
        }
    }
    // Other methods based on data access requirements
}
