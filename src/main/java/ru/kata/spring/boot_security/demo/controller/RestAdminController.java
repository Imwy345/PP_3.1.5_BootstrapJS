package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAdminController {

    private final UserService userService;
    private final RoleService roleService;

    public RestAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        System.out.println(user.getRoles());
        user.setRoles(roleService.checkRoles(user.getRoles()));
        System.out.println(user.getRoles());
        userService.saveUser(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/authentication")
    public ResponseEntity<User> authenticationUser (Authentication authentication) {
        return new ResponseEntity<>(userService.findByUsername(authentication.getName()),HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public  ResponseEntity<User> findUserById(@PathVariable("id") Long id){

        User user = userService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @DeleteMapping("/delete/")
    public ResponseEntity<String> deleteUser(@RequestBody  User user) {
        try {
            userService.removeUser(user.getId());
            return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/update/")
    public ResponseEntity<String> updateUser(@RequestBody  User user){
        user.setRoles(roleService.checkRoles(user.getRoles()));
            userService.updateUser(user);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }
}