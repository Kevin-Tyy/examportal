package org.examportal.menu;

import org.examportal.config.AuthenticatedUser;
import org.examportal.entities.*;
import org.examportal.enums.Category;
import org.examportal.services.CourseService;
import org.examportal.services.ExamService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeachersMenu {
    private static ExamService examService = null;
    private static CourseService courseService = null;

    public TeachersMenu(ExamService examService, CourseService courseService) {
        TeachersMenu.examService = examService;
        TeachersMenu.courseService = courseService;
    }

    public void createExam() {
        Scanner scanner = new Scanner(System.in);

        List<Course> allCourses = courseService.getAllCourses();

        if (allCourses.size() == 0) {
            System.out.println("No Courses found. Creating a new course...");
            Course course = new Course();
            createCourse(course, scanner);

            //re-fetch all courses
            allCourses = courseService.getAllCourses();
        }

        System.out.println("Select course name by Course ID:");
        printAllCourses(allCourses);

        // Select or create a course
        System.out.println("Enter the course ID for this exam (or 0 to create a new course):");
        long courseId = scanner.nextLong();
        scanner.nextLine(); // Consume newline character
        Course course;

        if (courseId == 0) {
            course = new Course();
            createCourse(course, scanner);

        } else {
            course = courseService.getCourseById(courseId);
            if (course == null) {
                String redText = "\u001B[31m";
                String resetText = "\u001B[0m";
                System.out.println(redText + "Course with ID: " + courseId + " not found. \nSelect a valid Course ID\n" + resetText);
                createExam();
            }
        }

        // Create a new exam
        Exam exam = new Exam();

        // Read exam details from user
        System.out.print("\nEnter exam title: ");
        String examTitle = scanner.nextLine();
        exam.setTitle(examTitle);

        System.out.print("Enter exam description: ");
        String examDescription = scanner.nextLine();
        exam.setDescription(examDescription);

        exam.setCourse(course);
        exam.setCreator(AuthenticatedUser.getInstance().getUser());

        // Read and add questions to the exam
        List<Question> questions = new ArrayList<Question>();
        boolean addMoreQuestions = true;

        int questionNumber = 1;
        while (addMoreQuestions) {
            Question question = new Question();

            System.out.print("Enter question " + questionNumber + " text: ");
            String questionText = scanner.nextLine();
            question.setText(questionText);
            question.setExam(exam);

            // Read and add choices to the question
            List<Choice> choices = new ArrayList<>();
            boolean addMoreChoices = true;
            while (addMoreChoices) {
                Choice choice = new Choice();

                System.out.print("Enter choice text: ");
                String choiceText = scanner.nextLine();
                choice.setText(choiceText);

                System.out.print("Is this choice correct? (yes/no): ");
                boolean isCorrect = scanner.nextLine().equalsIgnoreCase("yes");

                choice.setCorrect(isCorrect);
                choice.setQuestion(question);

                choices.add(choice);

                System.out.print("Add another choice? (yes/no): ");
                addMoreChoices = scanner.nextLine().equalsIgnoreCase("yes");
            }

            question.setChoices(choices);
            questions.add(question);

            System.out.print("\nAdd another question? (yes/no): ");
            addMoreQuestions = scanner.nextLine().equalsIgnoreCase("yes");

            questionNumber++;
        }
        exam.setQuestions(questions);
        // Close scanner
        scanner.close();
        // Print the exam details
        System.out.println("\u001B[34m" + "\nCreating the following exam:" + "\u001B[0m");
        System.out.println("Title: " + exam.getTitle());
        System.out.println("Description: " + exam.getDescription());
        System.out.println("Course: " + exam.getCourse().getName());
        System.out.println("Category: " + exam.getCourse().getCategory().name());
        System.out.println("Questions:");

        for (Question question : exam.getQuestions()) {
            System.out.println("- " + question.getText());
            System.out.println("  Choices:");

            int choiceIndex = 1;
            for (Choice choice : question.getChoices()) {
                System.out.println(choiceIndex + ". " + choice.getText() + (choice.isCorrect() ? " (Correct)" : ""));
                choiceIndex++;
            }
        }
        examService.createExam(exam);

        System.out.println("\u001B[32m" + "Exam created successfully!✅" + "\u001B[0m");

    }

    void createCourse(Course course, Scanner scanner) {
        System.out.println("\u001B[34m" + "Creating new course..." + "\u001B[0m");
        System.out.print("Enter course name: ");
        course.setName(scanner.nextLine());

        System.out.println("\n All available course categories:");
        System.out.println("+--------------------------------+");

        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println(i + 1 + ". " + categories[i]);
        }
        System.out.print("Select Course category from available course categories: ");
        course.setCategory(categories[scanner.nextInt() - 1]);
        scanner.nextLine();
        courseService.createCourse(course);

        System.out.println("\u001B[32m" + "Course created successfully!✅" + "\u001B[0m");

    }

    void printAllCourses(List<Course> allCourses) {
        int maxNameLength = "Course Name".length();
        int maxCategoryLength = "Course Category".length();

        for (Course course : allCourses) {
            if (course.getName().length() > maxNameLength) {
                maxNameLength = course.getName().length();
            }
            if (course.getCategory().name().length() > maxCategoryLength) {  // Assuming category is a String
                maxCategoryLength = course.getCategory().name().length();
            }
        }
        //print courses table
        String format = "| %-10s | %-" + maxNameLength + "s | %-" + maxCategoryLength + "s | %-12s |\n";

        // Print table header
        String x = "+------------+-" + "-".repeat(maxNameLength) + "-+-" + "-".repeat(maxCategoryLength) + "-+--------------+";
        System.out.println(x);
        System.out.printf(format, "Course ID", "Course Name", "Course Category", "No. of Exams");
        System.out.println(x);

        // Print course data
        for (Course course : allCourses) {
            System.out.printf(format, course.getId(), course.getName(), course.getCategory(), course.getExams().size());
        }

        System.out.println(x);

    }

    void viewAllExams() {
        User authenticatedUser = AuthenticatedUser.getInstance().getUser();
        List<Exam> exams = examService.getExamsByCreator(authenticatedUser);

        System.out.println("\u001B[34m" + "\nList of all exams you created." + "\u001B[0m");
        if (exams.isEmpty()) {
            System.out.println("No exams found.");
        } else {
            printAllExams(exams);
        }
        //return control to main menu
        new MainMenu(examService, courseService).displayMenu();
    }

    public void viewStudentsResults() {
        Scanner scanner = new Scanner(System.in);

        // Display list of all exams
        System.out.println("\u001B[34m" + "\nList of all exams you created." + "\u001B[0m");
        List<Exam> allExams = examService.getAllExams();
        if (allExams.isEmpty()) {
            System.out.println("No exams found.");
        } else {
            printAllExams(allExams);
        }

        // Prompt teacher to select an exam
        System.out.print("Enter the Exam ID to view results: ");
        long examId = scanner.nextLong();
        Exam exam = examService.getExamById(examId);

        if (exam == null) {
            System.out.println("\u001B[31m" + "No exam found with ID: " + examId + "\nEnter a valid Id" + "\u001B[0m");
            viewStudentsResults();
        }

        // Fetch results for the selected exam
        List<Result> examResults = examService.getExamResults(examId);

        // Display results

        assert exam != null;
        System.out.println("\u001B[34m" + "\nResults for Exam: " + exam.getTitle() + "\u001B[0m");
        System.out.println("+------------+--------------------------------+-----------------+");
        System.out.printf("| %-10s | %-30s | %-15s |%n", "Student Id", "Student's name", "Marks");
        System.out.println("+------------+--------------------------------+-----------------+");
        for (Result result : examResults) {
            User student = result.getStudent();
            System.out.printf("| %-10s | %-30s | %-15s |%n", student.getId(), student.getUsername(), result.getMarks() + "%");

        }
        System.out.println("+------------+--------------------------------+-----------------+");

        scanner.close();
    }

    void printAllExams(List<Exam> examsList) {
        System.out.println("+------------+--------------------------------+----------------------------------------------------+");
        System.out.printf("| %-10s | %-30s | %-50s |%n", "Exam ID", "Exam Title", "Description");
        System.out.println("+------------+--------------------------------+----------------------------------------------------+");

        for (Exam exam : examsList) {
            System.out.printf("| %-10d | %-30s | %-50s |%n", exam.getId(), exam.getTitle(), exam.getDescription());
        }
        System.out.println("+------------+--------------------------------+----------------------------------------------------+");
    }
}
