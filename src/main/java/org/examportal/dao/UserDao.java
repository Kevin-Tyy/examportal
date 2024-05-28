package org.examportal.dao;

import org.examportal.entities.Result;
import org.examportal.entities.User;

public interface UserDao {
    void save(User user);
    User findByUsername(String username);
}
