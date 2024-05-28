package org.examportal.services.impl;

import org.examportal.dao.ExamDao;
import org.examportal.entities.Exam;
import org.examportal.entities.Result;
import org.examportal.entities.User;
import org.examportal.services.ExamService;

import java.util.List;

public class ExamServiceImpl implements ExamService {

    private final ExamDao examDao;

    public ExamServiceImpl(ExamDao examDao) {
        this.examDao = examDao;
    }

    @Override
    public Exam getExamById(Long examId) {
        return examDao.getExamById(examId);
    }

    @Override
    public List<Exam> getAllExams() {
        return examDao.getAllExams();
    }

    @Override
    public void createExam(Exam exam) {
        examDao.createExam(exam);
    }

    @Override
    public List<Exam> getExamsByCreator(User creator) {
        return examDao.getExamsByCreator(creator.getId());
    }

    @Override
    public void saveExamResults(Result result) {
        examDao.saveExamResult(result);
    }

    @Override
    public List<Result> getExamResults(long examId) {
        return examDao.getExamResults(examId);
    }

    @Override
    public List<Result> getUserExamResults(long userId) {
        return examDao.getUserExamResults(userId);
    }

    @Override
    public boolean hasUserAttemptedExam(long userId, long examId) {
        return examDao.hasUserAttemptedExam(userId, examId);
    }

}
