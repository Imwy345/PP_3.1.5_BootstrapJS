package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;

    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;

    }

    @GetMapping("/")
    public String showAllUsers(Model model) {
       model.addAttribute("users",userServiceImpl.getAllUsers());
        return "admin";
    }
    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userServiceImpl.getUserById(id));
        return "userAdmin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id){
        userServiceImpl.removeUser(id);
        return "redirect:/admin/";
    }
}