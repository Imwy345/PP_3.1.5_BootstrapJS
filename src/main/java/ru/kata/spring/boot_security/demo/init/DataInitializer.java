package ru.kata.spring.boot_security.demo.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
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
        createUser("admin", "admin", Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
        createUser("user", "user", Collections.singletonList("ROLE_USER"));
    }

    private void createUser(String username, String password, List<String> roleNames) {
        User user = userService.findByUsername(username);
        Set<Role> roles = null;
        if (user == null) {
            roles = roleService.validateRoles(roleNames);
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(roles);

        userService.saveUser(user);
    }
}

