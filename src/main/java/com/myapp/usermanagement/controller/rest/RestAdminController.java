package com.myapp.usermanagement.controller.rest;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    private final UserAccountService userAccountService;

    public RestAdminController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserAccount>> showAllUsers() {
        List<UserAccount> users = userAccountService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by username
    @GetMapping("/users/username/{username}")
    public ResponseEntity<?> showUserByUsername(
            @PathVariable String username) {

        UserAccount user = userAccountService.fetchByUsername(username);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    // Create user
    @PostMapping("/users")
    public ResponseEntity<String> createUser(
            @RequestBody UserAccountDTO accountDTO) {

        userAccountService.createUser(accountDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User created successfully");
    }

    // Get user for editing
    @GetMapping("/users/{id}")
    public ResponseEntity<?> showEditUserForm(
            @PathVariable Long id) {

        UserAccount user = userAccountService.fetchById(id);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    // Update user
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccountDTO accountDTO) {

        userAccountService.updateUserInformation(id, accountDTO);

        return ResponseEntity.ok("User updated successfully");
    }

    // Soft delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> softDeleteUser(
            @PathVariable Long id) {

        boolean deleted = userAccountService.softDeleteUser(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok("User deactivated successfully");
    }

    // Hard delete user
    @DeleteMapping("/users/{id}/hard")
    public ResponseEntity<String> hardDeleteUser(
            @PathVariable Long id) {

        boolean deleted = userAccountService.hardDeleteUser(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok("User deleted permanently");
    }

    // Cleanup inactive users
    @DeleteMapping("/cleanup")
    public ResponseEntity<String> cleanupInactiveUsers(
            @RequestParam(defaultValue = "30") int days) {

        int deletedCount =
                userAccountService.permanentlyDeleteInactiveUsers(days);

        return ResponseEntity.ok(
                deletedCount + " users permanently deleted"
        );
    }
}