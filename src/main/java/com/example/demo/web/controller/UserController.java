package com.example.demo.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/secure-endpoint")
    public String secureEndpoint() {
        return "This is a secure endpoint accessible only by users with ROLE_USER.";
    }
}