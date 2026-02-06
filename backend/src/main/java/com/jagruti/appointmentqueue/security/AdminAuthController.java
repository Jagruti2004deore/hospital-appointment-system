package com.jagruti.appointmentqueue.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/admin")
public class AdminAuthController {

    private final JwtService jwtService;

    private static final String ADMIN_EMAIL = "admin@hospital.com";
    private static final String ADMIN_PASSWORD = "admin123";

    public AdminAuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {

        if (
            ADMIN_EMAIL.equals(request.getEmail()) &&
            ADMIN_PASSWORD.equals(request.getPassword())
        ) {
            String token = jwtService.generateToken(
                ADMIN_EMAIL,
                "ROLE_ADMIN"
            );
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Invalid admin credentials");
    }
}
