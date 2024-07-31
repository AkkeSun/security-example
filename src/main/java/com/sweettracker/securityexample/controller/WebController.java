package com.sweettracker.securityexample.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping("/login-page")
    public String login(Model model,
        @RequestParam(value = "loginFail", required = false, defaultValue = "false") boolean loginFail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
            !(authentication.getPrincipal() instanceof String &&
                authentication.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/";
        }

        if (loginFail) {
            model.addAttribute("loginFail", true);
        }
        return "login";
    }

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/error/access-denied")
    public String accessDeniedPage() {
        return "accessDenied";
    }
}
