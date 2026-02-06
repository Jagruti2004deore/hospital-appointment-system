package com.jagruti.appointmentqueue.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jagruti.appointmentqueue.dto.LoginRequest;
import com.jagruti.appointmentqueue.dto.RegisterRequest;
import com.jagruti.appointmentqueue.security.JwtService;
import com.jagruti.appointmentqueue.user.User;
import com.jagruti.appointmentqueue.user.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;   // ✅ interface
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, // ✅ interface
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // --------------------
    // REGISTER
    // --------------------
    public String register(RegisterRequest request) {

        String hashedPassword = passwordEncoder.encode(request.password);

        // default role = USER
        User user = new User(
                request.email,
                hashedPassword,
                "ROLE_USER"
        );

        userRepository.save(user);
        return "REGISTER OK";
    }

    // --------------------
    // LOGIN
    // --------------------
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 🔐 JWT with ROLE
        return jwtService.generateToken(user.getEmail(), user.getRole());
    }
}
