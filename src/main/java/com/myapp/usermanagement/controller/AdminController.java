package com.myapp.usermanagement.controller;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class AdminController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/users")
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        List<UserAccount> users = userAccountService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long id) {
        UserAccount user = userAccountService.fetchById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<UserAccount> getUserByUsername(@PathVariable String username) {
        UserAccount user = userAccountService.fetchByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> hardDeleteUser(@PathVariable Long userId) {
        boolean success = userAccountService.hardDeleteUser(userId);
        return success
                ? ResponseEntity.ok("User permanently deleted.")
                : ResponseEntity.badRequest().body("User not found or already deleted.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long id) {
        boolean deleted = userAccountService.softDeleteUser(id);
        return deleted
                ? ResponseEntity.ok("User has been deactivated.")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<String> cleanupInactiveUsers(@RequestParam(defaultValue = "30") int days) {
        int deletedCount = userAccountService.permanentlyDeleteInactiveUsers(days);
        return ResponseEntity.ok(deletedCount + " users permanently deleted.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccountDTO accountDTO) {
        UserAccount updatedUser = userAccountService.updateUserInformation(id, accountDTO);
        return (updatedUser != null) ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserAccount> createUser(@RequestBody UserAccountDTO accountDTO) {
        UserAccount createdUser = userAccountService.createUser(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
