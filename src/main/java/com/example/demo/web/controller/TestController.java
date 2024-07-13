package com.example.demo.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {
    @GetMapping("/protected")
    public String getProtectedResource(@AuthenticationPrincipal String username) {
        return "Hello " + username + ", this is a protected resource.";
    }

}
