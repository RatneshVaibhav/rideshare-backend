package com.example.rideshare.controller;

import com.example.rideshare.dto.*;
import com.example.rideshare.model.User;
import com.example.rideshare.repository.UserRepository;
import com.example.rideshare.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    // --------------------- REGISTER ------------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        String msg = request.getRole().equals("ROLE_DRIVER")
                ? "Driver registered successfully"
                : "User registered successfully";

        return ResponseEntity.ok(msg);
    }

    // ----------------------- LOGIN -------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        // authenticate + generate token
        String token = authService.login(request);

        // fetch user from DB
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        String role = (user != null) ? user.getRole() : "UNKNOWN";

        String msg = role.equals("ROLE_DRIVER")
                ? "Driver login successful"
                : "User login successful";

        // return proper JSON with all fields
        AuthResponse response = new AuthResponse(token, role, msg);

        return ResponseEntity.ok(response);
    }
}
