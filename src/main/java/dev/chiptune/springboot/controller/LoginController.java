package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.error.CustomNotFoundException;
import dev.chiptune.springboot.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class LoginController {

    @Autowired
    UsersService usersService;


    @GetMapping("/home")
    String home() {
        return "home";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/sample")
    String users(Model model) {
        model.addAttribute("data", usersService.findAllUsers());
        return "sample";
    }

    @GetMapping("/admin/home")
    String adminHome(Model model) {
        model.addAttribute("data", usersService.findAllUsers());
        return "/admin/home";
    }

    @GetMapping("/user/home")
    String userHome(Model model) {
        model.addAttribute("data", usersService.findAllUsers());
        return "/user/home";
    }
}
