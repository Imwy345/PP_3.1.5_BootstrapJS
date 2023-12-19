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
public class AdminControllerJS {

    private final UserService userService;
    private final RoleService roleService;

    public AdminControllerJS(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/saveUser")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        System.out.println(user.getRoles());
        user.setRoles(roleService.proverkaRoles(user.getRoles()));
        System.out.println(user.getRoles());
        userService.saveUser(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/authenticationUser")
    public ResponseEntity<User> getEmptyUser(Authentication authentication) {
        return new ResponseEntity<>(userService.findByUsername(authentication.getName()),HttpStatus.OK);
    }

    @GetMapping("/findUserById/{id}")
    public  ResponseEntity<User> findUserById(@PathVariable("id") Long id){

        User user = userService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        try {
            User user = userService.getUserById(id);
            System.out.println(user.getUsername());
            userService.removeUser(id);
            return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/updateUser/")
    public ResponseEntity<String> updateUser(@RequestBody  User user){
        User originUser = userService.getUserById(user.getId());
        originUser.setRoles(roleService.proverkaRoles(user.getRoles()));
        user.setRoles(roleService.proverkaRoles(user.getRoles()));
        if (originUser.getPassword().equals(user.getPassword())) {
            userService.updateUser(user);
        } else {
            originUser.setPassword(user.getPassword());
            userService.saveUser(user);
        }
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);

    }
}