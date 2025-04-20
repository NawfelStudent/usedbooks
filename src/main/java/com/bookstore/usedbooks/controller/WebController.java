package com.bookstore.usedbooks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/auth/signin")
    public String showSignInForm() {
        return "auth/signin";
    }
    
    @GetMapping("/auth/signup")
    public String showSignUpForm() {
        return "auth/signup";
    }
}