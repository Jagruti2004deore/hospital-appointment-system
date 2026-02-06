package com.jagruti.appointmentqueue.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/secure")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String secure() {
        return "SECURE ENDPOINT OK 🔐";
    }
}
