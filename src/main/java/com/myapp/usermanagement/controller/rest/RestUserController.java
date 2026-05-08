package com.myapp.usermanagement.controller.rest;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    private final UserAccountService userAccountService;

    public RestUserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserAccount user = userAccountService.fetchById(id);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserAccount>> showAllUsers() {
        List<UserAccount> users = userAccountService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }

    // Update profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccountDTO accountDTO) {

        userAccountService.updateUserInformation(id, accountDTO);

        return ResponseEntity.ok("User updated successfully");
    }

    // Request account deletion
    @PostMapping("/{userId}/delete")
    public ResponseEntity<String> requestAccountDeletion(
            @PathVariable Long userId) {

        userAccountService.requestAccountDeletion(userId);

        return ResponseEntity.ok("Account deletion requested");
    }

    // Recover account
    @PostMapping("/{userId}/recover")
    public ResponseEntity<String> recoverAccount(
            @PathVariable Long userId) {

        userAccountService.recoverAccount(userId);

        return ResponseEntity.ok("Account recovered");
    }
}