package com.themis.themis_backend.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@JsonTypeName("ANONIMA")
public class DenunciaAnonimaResponseDTO extends DenunciaResponseDTO {

    private String correo;

    public DenunciaAnonimaResponseDTO() {
        super();
    } 
    
    public DenunciaAnonimaResponseDTO(Long id, String codigoDenuncia, String estado, LocalDate fechaIncidente, LocalTime horaIncidente, boolean esAhora, String departamento, String provincia, String distrito, String descripcionHechos, List<String> rutasArchivos, LocalDateTime fechaRegistro, boolean anonimo) {
        super(id, codigoDenuncia, estado, fechaIncidente, horaIncidente, esAhora, departamento, provincia, distrito, descripcionHechos, rutasArchivos, fechaRegistro, anonimo);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
