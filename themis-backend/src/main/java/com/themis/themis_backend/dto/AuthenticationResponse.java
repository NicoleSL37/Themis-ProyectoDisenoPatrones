package com.themis.themis_backend.dto;

import com.themis.themis_backend.model.Rol;
import java.util.Set;

public class AuthenticationResponse {

    private String token;
    private String email; // O email
    private Long userId;
    private Set<Rol> roles;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, String email, Long userId, Set<Rol> roles) {
        this.token = token;
        this.email = email;
        this.userId = userId;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
