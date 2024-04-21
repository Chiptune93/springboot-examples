package dev.chiptune.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorPageController {
    @GetMapping("/login-required")
    public String loginRequired(Model model) {
        return "/error/login_required";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        return "/error/access_denied";
    }
}
