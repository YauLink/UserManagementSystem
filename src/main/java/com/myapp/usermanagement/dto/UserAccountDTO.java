package com.myapp.usermanagement.dto;

import com.myapp.usermanagement.model.UserAccount;

import java.time.LocalDateTime;

public class UserAccountDTO {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private UserAccount.Role role;
    private UserAccount.Status status;

    private LocalDateTime created;

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserAccount.Role getRole() {
        return role;
    }

    public void setRole(UserAccount.Role role) {
        this.role = role;
    }

    public UserAccount.Status getStatus() {
        return status;
    }

    public void setStatus(UserAccount.Status status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
