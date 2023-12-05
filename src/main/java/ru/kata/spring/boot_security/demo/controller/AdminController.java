package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/new")
    public String showRegistrationForm(Model model) {
//        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "addNewUser";
    }

    @PostMapping("/new")
    public String registerUser(@ModelAttribute("userForm") @Valid User userForm,
                               @RequestParam("roles") List<String> roleNames,
                               BindingResult bindingResult, Model model) {
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            model.addAttribute("roles",roleService.getAllRoles());
            return "addNewUser";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles",roleService.getAllRoles());
            return "addNewUser";
        }
        if (!userService.saveUser(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            model.addAttribute("roles",roleService.getAllRoles());
            return "addNewUser";
        } else {
            userForm.setRoles(roleService.validateRoles(roleNames));
            return "redirect:/admin/";
        }
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "userAdmin";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        Set<Role> userRoles = user.getRoles();
        List<String> roleNames = new ArrayList<>();

        for (Role role : userRoles) {
            roleNames.add(role.getName());
        }

        model.addAttribute("userForm", user);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("userRoles", roleNames);

        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("userForm") @Valid User userForm,
                             @RequestParam("roles") List<String> roleNames,
                             @PathVariable("id") Long userId) {

        User user = userService.getUserById(userId);
        user.setRoles(roleService.validateRoles(roleNames));
        user.setUsername(userForm.getUsername());
        if (user.getPassword().equals(userForm.getPassword())) {
            userService.updateUser(user);
        } else {
            user.setPassword(userForm.getPassword());
            userService.saveUser(user);
        }
        return "redirect:/admin/";

    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/";
    }
}