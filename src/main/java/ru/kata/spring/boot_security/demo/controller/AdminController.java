package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;

        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping("/")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userServiceImpl.getAllUsers());
        return "admin";
    }

    @GetMapping("/new")
    public String showRegistrationForm(Model model) {
        List<Role> roles = roleServiceImpl.getAllRoles();
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roles);
        return "addNewUser";
    }

    @PostMapping("/new")
    public String registerUser(@ModelAttribute("userForm") @Valid User userForm,
                               @RequestParam("roles") List<String> roleNames,
                               BindingResult bindingResult, Model model) {
        userForm.setRoles(roleServiceImpl.validateRoles(roleNames));
        if (bindingResult.hasErrors()) {
            return "addNewUser";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "addNewUser";
        }
        if (!userServiceImpl.saveUser(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "addNewUser";
        } else {
            return "redirect:/admin/";
        }
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userServiceImpl.getUserById(id));
        return "userAdmin";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userServiceImpl.getUserById(id);
        Set<Role> userRoles = user.getRoles();
        List<String> roleNames = new ArrayList<>();

        for (Role role : userRoles) {
            roleNames.add(role.getName());
        }

        model.addAttribute("userForm", user);
        model.addAttribute("roles", roleServiceImpl.getAllRoles());
        model.addAttribute("userRoles", roleNames);

        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("userForm") @Valid User userForm,
                             @RequestParam("roles") List<String> roleNames,
                             @PathVariable("id") Long userId) {

        User user = userServiceImpl.getUserById(userId);
        user.setRoles(roleServiceImpl.validateRoles(roleNames));
        user.setUsername(userForm.getUsername());
        if (user.getPassword().equals(userForm.getPassword())) {
            user.setPassword(userForm.getPassword());
            userServiceImpl.updateUser(user);
        } else {
            user.setPassword(userForm.getPassword());
            userServiceImpl.saveUser(user);
        }
        return "redirect:/admin/";

    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userServiceImpl.removeUser(id);
        return "redirect:/admin/";
    }
}