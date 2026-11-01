package com.myapp.usermanagement.controller.rest;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    private final UserAccountService userAccountService;

    public RestUserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
        UserAccount user = userAccountService.fetchById(id);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
        if (!user.getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(user);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserAccount>> showAllUsers(Authentication authentication) {
        UserAccount user = userAccountService.fetchByUsername(authentication.getName());
        return ResponseEntity.ok(user == null ? List.of() : List.of(user));
    }

    // Update profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccountDTO accountDTO,
            Authentication authentication) {

        UserAccount existing = userAccountService.fetchById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        if (!existing.getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        userAccountService.updateUserInformation(id, accountDTO);

        return ResponseEntity.ok("User updated successfully");
    }

    // Request account deletion
    @PostMapping("/{userId}/delete")
    public ResponseEntity<String> requestAccountDeletion(
            @PathVariable Long userId,
            Authentication authentication) {

        UserAccount existing = userAccountService.fetchById(userId);
        if (existing == null) return ResponseEntity.notFound().build();
        if (!existing.getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        userAccountService.requestAccountDeletion(userId);

        return ResponseEntity.ok("Account deletion requested");
    }

    // Recover account
    @PostMapping("/{userId}/recover")
    public ResponseEntity<String> recoverAccount(
            @PathVariable Long userId,
            Authentication authentication) {

        UserAccount existing = userAccountService.fetchById(userId);
        if (existing == null) return ResponseEntity.notFound().build();
        if (!existing.getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        userAccountService.recoverAccount(userId);

        return ResponseEntity.ok("Account recovered");
    }
}
