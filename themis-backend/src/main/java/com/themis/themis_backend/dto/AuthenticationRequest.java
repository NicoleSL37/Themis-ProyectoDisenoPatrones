package com.themis.themis_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationRequest {

    @NotBlank(message = "El nombre de usuario/correo no puede estar vacío")
    private String username; // O email, dependiendo de cómo manejes el login

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

}
