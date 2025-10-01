package com.example.employee_management_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (role.equals("ROLE_HR")) {
            return "redirect:/hr/dashboard";
        } else {
            return "redirect:/employees/dashboard";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // points to templates/access-denied.html
    }

}
