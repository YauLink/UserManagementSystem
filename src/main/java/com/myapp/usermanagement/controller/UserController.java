package com.myapp.usermanagement.controller;

import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserController {
    private final UserAccountService userAccountService;

    @Autowired
    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
}
