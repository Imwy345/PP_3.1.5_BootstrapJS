package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.lang.reflect.Array;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    public DataInitializer( UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @Override
    public void run(String... args) {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        createRole("ROLE_ADMIN");
        createRole("ROLE_USER");
    }

    private void createRole(String roleName) {
        Role role = roleService.findRoleByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleService.saveRole(role);
        }
    }


    private void initializeUsers() {
        createUser("admin", "admin", Arrays.asList("ROLE_ADMIN","ROLE_USER"));
        createUser("user", "user", Collections.singletonList("ROLE_USER"));
    }

    private void createUser(String username, String password, List<String> roleNames) {
        User user = userService.findByUsername(username);
        Set<Role> roles = null;
        if( user == null) {
            roles = roleService.validateRoles(roleNames);
        }
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRoles(roles);

            userService.saveUser(user);
        }
    }

