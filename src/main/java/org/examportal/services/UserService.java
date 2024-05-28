package org.examportal.services;

import org.examportal.entities.Result;
import org.examportal.entities.User;

public interface UserService {
    void registerUser (User user);
    User loginUser(User user);
    boolean isUsernameAvailable(String username);
}
