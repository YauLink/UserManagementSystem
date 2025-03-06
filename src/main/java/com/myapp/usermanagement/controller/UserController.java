package com.myapp.usermanagement.controller;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserAccountService userAccountService;

    @Autowired
    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long id) {
        UserAccount user = userAccountService.fetchById(id);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserAccount> getUserByUsername(@PathVariable String username) {
        UserAccount user = userAccountService.fetchByUsername(username);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccountDTO accountDTO) {
        UserAccount updatedUser = userAccountService.updateUserInformation(id, accountDTO);
        return (updatedUser != null) ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{userId}/delete")
    public ResponseEntity<String> requestAccountDeletion(@PathVariable Long userId) {
        boolean success = userAccountService.requestAccountDeletion(userId);
        return success
                ? ResponseEntity.ok("Account deletion requested. You can recover your account within N days.")
                : ResponseEntity.badRequest().body("User not found.");
    }

    @PostMapping("/{userId}/recover")
    public ResponseEntity<String> recoverAccount(@PathVariable Long userId) {
        boolean success = userAccountService.recoverAccount(userId);
        return success
                ? ResponseEntity.ok("Account successfully recovered.")
                : ResponseEntity.badRequest().body("Account recovery failed. Account may not exist or is already deleted.");
    }
}
