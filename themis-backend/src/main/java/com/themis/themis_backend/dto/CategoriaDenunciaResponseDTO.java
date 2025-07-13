package com.themis.themis_backend.dto;

import java.time.LocalDateTime;

public class CategoriaDenunciaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaRegistro; // <-- Nuevo campo
    private LocalDateTime fechaActualizacion;

    public CategoriaDenunciaResponseDTO() {
    }

    // Constructor con todos los campos (opcional)
    public CategoriaDenunciaResponseDTO(Long id, String nombre, String descripcion, LocalDateTime fechaRegistro, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

}
