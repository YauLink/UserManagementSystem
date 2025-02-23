package com.myapp.usermanagement.controller;

import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AdminController {

    @Autowired
    private UserAccountService userAccountService;

    public ResponseEntity<List<UserAccount>> getAllUsers() {
        List<UserAccount> users = userAccountService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }
}
