package com.myapp.usermanagement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthResponseDTO {

    private Long userId;

    private String fullName;

    private String message;

    private String token;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(
            Long userId,
            String fullName,
            String message) {

        this.userId = userId;
        this.fullName = fullName;
        this.message = message;
    }

    public AuthResponseDTO(
            Long userId,
            String fullName,
            String message,
            String token) {

        this.userId = userId;
        this.fullName = fullName;
        this.message = message;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}