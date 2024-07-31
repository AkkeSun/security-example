package com.sweettracker.securityexample.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/user")
    public Authentication user() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/admin")
    public String admin() {
        return "hello Admin";
    }

    @GetMapping("/user-and-admin")
    public String userAndAdmin() {
        return "hello User And Admin";
    }

    @GetMapping("/all")
    public String all() {
        return "hello all";
    }
}
