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
                               @RequestParam("roles") List<String> roleNames) {
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

    @PutMapping ("/")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             @RequestParam("roles") List<String> roleNames,
                             @RequestParam long id) {

        User originUser = userService.getUserById(id);
        originUser.setRoles(roleService.validateRoles(roleNames));
        originUser.setUsername(user.getUsername());
        if (originUser.getPassword().equals(user.getPassword())) {
            userService.updateUser(originUser);
        } else {
            originUser.setPassword(user.getPassword());
            userService.saveUser(originUser);
        }
        return "redirect:/admin/";

    }

    @DeleteMapping("/")
    public String deleteUser(@RequestParam long id) {
        userService.removeUser(id);
        return "redirect:/admin/";
    }
}

