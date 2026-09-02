package com.myapp.usermanagement.dto;

public class AuthResponseDTO {

    private Long userId;
    private String fullName;
    private String message;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(Long userId, String fullName, String message) {
        this.userId = userId;
        this.fullName = fullName;
        this.message = message;
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
}