package org.examportal.services.impl;

import org.examportal.dao.UserDao;
import org.examportal.entities.User;
import org.examportal.services.UserService;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void registerUser(User user) {
        if (isUsernameAvailable(user.getUsername())) {
            return; // Username already exists
        }
        // Set default role or other properties if needed
        userDao.save(user);
    }

    @Override
    public User loginUser(User user) {
        User existingUser = userDao.findByUsername(user.getUsername());
        if (existingUser != null) {
            if (existingUser.getPassword().equals(user.getPassword())) {
                return existingUser;
            }
            System.out.println("\u001B[31m" + "Incorrect credentials!" + "\u001B[0m");
        } else {
            System.out.println("\u001B[31m" + "User not found!!" + "\u001B[0m");
        }
        return null; // Authentication failed

    }

    @Override
    public boolean isUsernameAvailable(String username) {
        User existingUser = userDao.findByUsername(username);
        return existingUser != null;
    }
}
