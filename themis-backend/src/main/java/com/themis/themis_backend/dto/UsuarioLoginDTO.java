package com.themis.themis_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioLoginDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String email;

    @NotBlank(message = "La contrasenia no puede estar vacía")
    private String password;

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
