package ru.kata.spring.boot_security.demo.service;


import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    void removeUser(long id);

    void updateUser(User user);

    void saveUser(User user);

    User findByUsername(String username);

}