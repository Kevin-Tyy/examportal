package org.examportal.menu;

import org.examportal.config.AuthenticatedUser;
import org.examportal.entities.User;
import org.examportal.enums.Role;
import org.examportal.services.UserService;

import java.util.Scanner;

public class AuthMenu {
    private final UserService userService;

    public AuthMenu(UserService userService) {
        this.userService = userService;
    }

    public boolean displayAuthMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isAuthenticated = false;

        while (!isAuthenticated) {
            System.out.println("Select an option:");
            System.out.println("1. Register a new account");
            System.out.println("2. Login into your account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> registerUser(scanner);
                case 2 -> {
                    User loggedInUser = loginUser(scanner);
                    if (loggedInUser != null) {
                        isAuthenticated = true;
                        System.out.println("\u001B[34m" + "\nLogin successful. Welcome, " + loggedInUser.getUsername() + "!" + "\u001B[0m");
                    } else {
                        System.out.println("\u001B[31m" + "Login failed. Please try again.\n" + "\u001B[0m");
                    }
                }
                case 3 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        return isAuthenticated;
    }

    private void registerUser(Scanner scanner) {
        System.out.println("\u001B[34m" + "\nCreate a new Examportal account:" + "\u001B[0m");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (userService.isUsernameAvailable(username)) {
            System.out.println("\u001B[31m" + "Username already exists. Please choose a different username.\n" + "\u001B[0m");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Role[] roles = Role.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println(i + 1 + ". " + roles[i]);
        }
        System.out.print("Select role: ");

        user.setRole(roles[scanner.nextInt() - 1]);
        userService.registerUser(user);

        System.out.println("\u001B[32m" + "User registered successfully. Login into your account!\n" + "\u001B[0m");
    }

    private User loginUser(Scanner scanner) {
        System.out.println("\u001B[34m" + "\nLogin into your Examportal account:" + "\u001B[0m");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        User loggedInUser = userService.loginUser(user);
        if (loggedInUser != null) {
            AuthenticatedUser.getInstance().setUser(loggedInUser); // Set the authenticated user
        }
        return loggedInUser;
    }
}
