package ru.kata.spring.boot_security.demo.configs;

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

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    public DataInitializer(BCryptPasswordEncoder bCryptPasswordEncoder, UserServiceImpl userService, RoleServiceImpl roleService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }
    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        createRole("ROLE_USER");
        createRole("ROLE_ADMIN");
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
        createUser("user", "user", Collections.singletonList("ROLE_USER"));
        createUser("admin", "admin", Arrays.asList("ROLE_ADMIN","ROLE_USER"));
    }

    private void createUser(String username, String password, List<String> roleNames) {
        User user = userService.findByUsername(username);
        if (user == null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : roleNames) {
                Role role = roleService.findRoleByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }

            user = new User();
            user.setUsername(username);

            if (bCryptPasswordEncoder != null) {
                String encodedPassword = bCryptPasswordEncoder.encode(password);
                user.setPassword(encodedPassword);
            }

            user.setRoles(roles);

            userService.saveUser(user);
        }
    }

}
