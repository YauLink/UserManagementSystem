package com.myapp.usermanagement.controller;

import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    //TODO user creation, info update(including roles and status), soft/hard delete
}
