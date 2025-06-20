package com.example.ecommerce_project.security.request;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email(message = "Email should be validsignup")
    private String email;

    private Set<String> role;

    @NotBlank
    private String password;

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }}
