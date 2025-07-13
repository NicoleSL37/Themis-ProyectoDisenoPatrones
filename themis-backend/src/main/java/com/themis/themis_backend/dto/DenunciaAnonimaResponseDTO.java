package com.themis.themis_backend.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ANONIMA")
public class DenunciaAnonimaResponseDTO extends DenunciaResponseDTO {

    private String correo;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
