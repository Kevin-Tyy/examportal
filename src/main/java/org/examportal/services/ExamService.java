package org.examportal.services;

import org.examportal.entities.Exam;
import org.examportal.entities.Result;
import org.examportal.entities.User;

import java.util.List;

public interface ExamService {
    Exam getExamById(Long examId);
    List<Exam> getAllExams();
    void createExam(Exam exam);
    List<Exam> getExamsByCreator (User creator);
    void saveExamResults(Result result);
    List<Result> getExamResults(long examId);
    List<Result> getUserExamResults(long userId);
    boolean hasUserAttemptedExam(long userId, long examId);
}
