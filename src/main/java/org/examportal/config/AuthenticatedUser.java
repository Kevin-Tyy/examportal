package org.examportal.config;

import org.examportal.entities.User;

public class AuthenticatedUser {
    private static AuthenticatedUser instance;
    private User user;

    private AuthenticatedUser() {
        // Private constructor to prevent instantiation
    }

    public static synchronized AuthenticatedUser getInstance() {
        if (instance == null) {
            instance = new AuthenticatedUser();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
