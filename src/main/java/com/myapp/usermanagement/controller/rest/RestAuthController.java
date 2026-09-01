package com.myapp.usermanagement.controller.rest;

import com.myapp.usermanagement.dto.AuthRequestDTO;
import com.myapp.usermanagement.dto.AuthResponseDTO;
import com.myapp.usermanagement.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RestAuthController {

    private final AuthService authService;

    public RestAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody AuthRequestDTO request) {

        AuthResponseDTO response =
                authService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(response);
    }
}