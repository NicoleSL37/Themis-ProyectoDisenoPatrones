package com.themis.themis_backend.dto;

import com.themis.themis_backend.model.Rol;
import java.util.Set;

public class AuthenticationResponse {

    private String token;
    private String username; // O email
    private Long userId;
    private Set<Rol> roles;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, String username, Long userId, Set<Rol> roles) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
