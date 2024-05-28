package org.examportal;

import org.examportal.dao.CourseDao;
import org.examportal.dao.UserDao;
import org.examportal.dao.impl.CourseDaoImpl;
import org.examportal.dao.impl.UserDaoImpl;
import org.examportal.menu.AuthMenu;
import org.examportal.menu.MainMenu;
import org.examportal.services.CourseService;
import org.examportal.services.UserService;
import org.examportal.services.impl.CourseServiceImpl;
import org.examportal.services.impl.UserServiceImpl;
import org.examportal.utils.HibernateUtility;
import org.examportal.dao.ExamDao;
import org.examportal.dao.impl.ExamDaoImpl;
import org.examportal.services.ExamService;
import org.examportal.services.impl.ExamServiceImpl;
import org.hibernate.SessionFactory;

public class ExamPortalApplication {
    private static ExamService examService = null;
    private static CourseService courseService = null;
    private static UserService userService = null;

    public ExamPortalApplication(ExamService examService, CourseService courseService, UserService userService) {
        ExamPortalApplication.examService = examService;
        ExamPortalApplication.courseService = courseService;
        ExamPortalApplication.userService = userService;
    }

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
        if (sessionFactory == null) {
            System.out.println("Something went wrong");
            return;
        }

        ExamDao examDao = new ExamDaoImpl(sessionFactory); // Instantiate appropriate DAO implementation
        ExamService examService = new ExamServiceImpl(examDao); // Inject DAO into service

        CourseDao courseDao = new CourseDaoImpl(sessionFactory);
        CourseService courseService = new CourseServiceImpl(courseDao);

        UserDao userDao = new UserDaoImpl(sessionFactory);
        UserService userService = new UserServiceImpl(userDao);

        ExamPortalApplication examPortalApplication = new ExamPortalApplication(examService, courseService, userService);

        AuthMenu authMenu = new AuthMenu(userService);

        System.out.println("\u001B[34m" + "\n WELCOME TO EXAM PORTAL");
        System.out.println("+----------------------+\n" + "\u001B[0m");

        boolean isAuthenticated = authMenu.displayAuthMenu();

        if (isAuthenticated) {

            // Pass control to MainMenu
            MainMenu mainMenu = new MainMenu(examService, courseService);
            mainMenu.displayMenu();

        } else {
            // Exit the program
            System.out.println("Authentication failed. Exiting...");
            System.exit(0);
        }

    }
}
