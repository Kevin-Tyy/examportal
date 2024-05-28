package org.examportal.menu;

import org.examportal.config.AuthenticatedUser;
import org.examportal.entities.*;
import org.examportal.services.ExamService;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {

    private static ExamService examService = null;

    public StudentMenu(ExamService examService) {
        StudentMenu.examService = examService;
    }

    public void takeExam() {
        Scanner scanner = new Scanner(System.in);
        // Retrieve the exam from the database using the examId
        List<Exam> allExams = examService.getAllExams();
        printAllExams(allExams);


        System.out.print("Enter exam_id of the exam you want to sit for: ");

        long examId = scanner.nextLong();
        Exam exam = examService.getExamById(examId);

        if (exam == null) {
            System.out.println("\u001B[31m" + "Exam not found with ID: " + examId + "\nSelect a valid Exam ID\n" + "\u001B[0m");
            takeExam();
        }
        if (examService.hasUserAttemptedExam(AuthenticatedUser.getInstance().getUser().getId(), examId)) {
            System.out.println("\u001B[31m" + "You have already attempted this exam.\n Select an exam you haven't attempted" + "\u001B[0m");
            takeExam();
        }

        assert exam != null;
        List<Question> questions = exam.getQuestions();
        int totalQuestions = questions.size();
        int correctAnswers = 0;

        Result result = new Result();
        result.setExam(exam);
        result.setStudent(AuthenticatedUser.getInstance().getUser());

        System.out.println("\n• Starting Exam: " + "\u001B[34m" + exam.getTitle() + "\u001B[0m");
        System.out.println("• Description: " + "\u001B[34m" + exam.getDescription() + "\u001B[0m");
        System.out.println("• Course: " + "\u001B[34m" + exam.getCourse().getName() + "\u001B[0m");
        System.out.println("• Category: " + "\u001B[34m" + exam.getCourse().getCategory().name() + "\u001B[0m");
        System.out.println("• Total Questions: " + "\u001B[34m" + totalQuestions + "\u001B[0m");

        // Loop through each question and present it to the user
        int questionNumber = 1;
        for (Question question : questions) {
            System.out.println("\u001B[34m" + "\nQuestion " + questionNumber + ": " + "\u001B[0m" + question.getText());

            List<Choice> choices = question.getChoices();
            Collections.shuffle(choices); // Randomize the order of choices

            // Present choices to the user
            for (int i = 0; i < choices.size(); i++) {
                System.out.println((i + 1) + ". " + choices.get(i).getText());
            }

            System.out.print("Enter your choice (1-" + choices.size() + "): ");
            int userChoiceIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Validate and check the correctness of the user's choice
            if (userChoiceIndex >= 1 && userChoiceIndex <= choices.size()) {
                Choice userChoice = choices.get(userChoiceIndex - 1); // Convert to 0-based index
                if (userChoice.isCorrect()) {
                    System.out.println("\u001B[32m" + "Correct!✅" + "\u001B[0m");
                    correctAnswers++;
                } else {
                    System.out.println("\u001B[31m" + "Incorrect!❌" + "\u001B[0m");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

            questionNumber++;

        }

        // Calculate and display the score
        double score = (double) correctAnswers / totalQuestions * 100;
        result.setMarks(score);
        examService.saveExamResults(result);


        System.out.println("\n• Exam Completed!");
        System.out.println("• Correct Answers: " + correctAnswers + " / " + totalQuestions);
        System.out.println("• Score: " + "\u001B[32m" + score + "%" + "\u001B[0m");

        // Close scanner
        scanner.close();
    }

    void viewAllResults() {
        List<Result> results = examService.getUserExamResults(AuthenticatedUser.getInstance().getUser().getId());

        if (results.isEmpty()) {
            System.out.println("No exams found.");
        } else {
            System.out.println("\u001B[34m" + "\nResults for All Exams You attempted:" + "\u001B[0m");
            System.out.println("+------------+------------------------+------------------------+------------------------+-----------------+");
            System.out.printf("| %-10s | %-22s | %-22s | %-22s | %-15s |%n", "Exam Id", "Exam Title", "Course", "Exam Creator", "Marks");
            System.out.println("+------------+------------------------+------------------------+------------------------+-----------------+");
            for (Result result : results) {
                System.out.printf("| %-10s | %-22s | %-22s | %-22s | %-15s |%n", result.getExam().getId(), result.getExam().getTitle(), result.getExam().getCourse().getName(), result.getExam().getCreator().getUsername(), result.getMarks() + "%");
            }
            System.out.println("+------------+------------------------+------------------------+------------------------+-----------------+");
        }
    }

    void printAllExams(List<Exam> allExams) {
        System.out.println("\u001B[34m" + "\nList of all exams you created." + "\u001B[0m");

        System.out.println("+------------+--------------------------------+----------------------------------------------------+");
        System.out.printf("| %-10s | %-30s | %-50s |%n", "Exam ID", "Exam Title", "Description");
        System.out.println("+------------+--------------------------------+----------------------------------------------------+");

        // Table rows
        allExams.forEach(exam -> System.out.printf("| %-10d | %-30s | %-50s |%n", exam.getId(), exam.getTitle(), exam.getDescription()));
        System.out.println("+------------+--------------------------------+----------------------------------------------------+");
    }
}
