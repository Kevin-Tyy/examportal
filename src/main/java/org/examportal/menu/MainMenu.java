package org.examportal.menu;

import org.examportal.config.AuthenticatedUser;
import org.examportal.entities.User;
import org.examportal.enums.Role;
import org.examportal.services.CourseService;
import org.examportal.services.ExamService;

import java.util.Scanner;

public class MainMenu {
    private final ExamService examService;
    private final CourseService courseService;

    public MainMenu(ExamService examService, CourseService courseService) {
        this.examService = examService;
        this.courseService = courseService;
    }

    public void displayMenu() {
        User authenticatedUser = AuthenticatedUser.getInstance().getUser();
        if (authenticatedUser == null) {
            System.out.println("\u001B[31m" + "No authenticated user found. Exiting..." + "\u001B[0m");
            System.exit(0);
        }


        if (authenticatedUser.getRole() == Role.TEACHER){
            System.out.println("\nSelect why you're here: \n1. Create an Exam\n2. View all your exams\n3. View Exam Results");
            System.out.print("\nEnter your choice: ");

            Scanner scanner = new Scanner(System.in);
            TeachersMenu menu = new TeachersMenu(examService, courseService);
            switch (scanner.nextInt()) {
                case 1 -> menu.createExam();
                case 2 -> menu.viewAllExams();
                case 3 -> menu.viewStudentsResults();
                default -> System.out.println("You entered an invalid choice...");
            }
            scanner.close();
        }else if (authenticatedUser.getRole() == Role.STUDENT) {
            System.out.println("\nSelect why you're here: \n1. Sit for exam\n2. View Your Results");
            System.out.print("\nEnter your choice: ");

            Scanner scanner = new Scanner(System.in);
            StudentMenu menu = new StudentMenu(examService);
            switch (scanner.nextInt()) {
                case 1 -> menu.takeExam();
                case 2 -> menu.viewAllResults();
                default -> System.out.println("You entered an invalid choice...");
            }
            scanner.close();
        }else{
            System.out.println("This User has an invalid role!!");
        }

    }
}
