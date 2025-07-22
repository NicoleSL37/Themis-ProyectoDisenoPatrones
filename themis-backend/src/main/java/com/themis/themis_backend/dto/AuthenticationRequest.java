package com.themis.themis_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationRequest {

    @NotBlank(message = "El nombre de usuario/correo no puede estar vacío")
    private String email; // O email, dependiendo de cómo manejes el login

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
