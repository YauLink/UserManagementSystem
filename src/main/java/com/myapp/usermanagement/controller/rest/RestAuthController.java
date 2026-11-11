package com.myapp.usermanagement.controller.rest;

import com.myapp.usermanagement.dto.AuthRequestDTO;
import com.myapp.usermanagement.dto.AuthResponseDTO;
import com.myapp.usermanagement.dto.RegistrationRequestDTO;
import com.myapp.usermanagement.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class RestAuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public RestAuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            AuthResponseDTO response = authService.login(
                    request.getUsername(), request.getPassword());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequestDTO request) {
        try {
            authService.register(request);
            return ResponseEntity.status(201).body("Registration successful");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
