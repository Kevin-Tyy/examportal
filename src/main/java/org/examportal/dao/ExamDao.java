package org.examportal.dao;

import org.examportal.entities.Exam;
import org.examportal.entities.Result;
import org.examportal.entities.User;

import java.util.List;

public interface ExamDao {
    Exam getExamById(Long examId);
    List<Exam> getAllExams();
    void createExam(Exam exam);
    List<Exam> getExamsByCreator(Long creatorId);
    List<Result> getExamResults(long examId);
    List<Result> getUserExamResults(long userId);
    void saveExamResult(Result result);
    boolean hasUserAttemptedExam(long userId, long examId);


    // Other methods based on data access requirements
}

