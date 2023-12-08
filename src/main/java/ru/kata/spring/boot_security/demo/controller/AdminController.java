package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
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
    public String showAllUsers(Model model, Authentication authentication) {

        model.addAttribute("user",userService.findByUsername(authentication.getName()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }



    @PostMapping("/")
    public String registerUser(@ModelAttribute("newUser") @Valid User newUser,
                               @RequestParam("roles") List<String> roleNames,
                               BindingResult bindingResult, Model model) {
//        if (!newUser.getPassword().equals(newUser.getPasswordConfirm())) {
//            model.addAttribute("passwordError", "Пароли не совпадают");
//            model.addAttribute("roles",roleService.getAllRoles());
//            return "addNewUser";
//        }
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("roles",roleService.getAllRoles());
//            return "addNewUser";
//        }
//        if (!userService.saveUser(newUser)) {
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
//            model.addAttribute("roles",roleService.getAllRoles());
//            return "addNewUser";
//        } else {
        if(newUser!=null ){
            System.out.println("Username:" +newUser.getUsername() + " Password: " +newUser.getPassword());
        }
        if (roleNames != null) {
            System.out.println("Selected roles: " + roleNames);
        }
            newUser.setRoles(roleService.validateRoles(roleNames));
            userService.saveUser(newUser);

            return "redirect:/admin/";
        }
//    }

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

    @DeleteMapping("/")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/";
    }
}
//    @GetMapping("/new")
//    public String showRegistrationForm(Model model) {
////        List<Role> roles = roleService.getAllRoles();
//        model.addAttribute("userForm", new User());
//        model.addAttribute("roles", roleService.getAllRoles());
//        return "addNewUser";
//    }