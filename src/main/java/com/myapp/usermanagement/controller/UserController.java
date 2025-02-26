package com.myapp.usermanagement.controller;

import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    //TODO user info update, soft/hard delete
}
