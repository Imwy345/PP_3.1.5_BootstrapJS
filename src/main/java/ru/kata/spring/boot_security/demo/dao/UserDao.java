package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User getUserById(long id);

    void removeUser(long id);

    void updateUser(User user);

    boolean saveUser(User user);

    User findByUsername(String username);
}
