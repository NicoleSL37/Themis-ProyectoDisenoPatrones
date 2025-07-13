package com.themis.themis_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DenunciaAnonimaRequestDTO {

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado; // Podrías usar un enum para esto

    @NotNull(message = "La fecha del incidente no puede ser nula")
    private LocalDate fechaIncidente;

    @NotNull(message = "La hora del incidente no puede ser nula")
    private LocalTime horaIncidente;

    private boolean esAhora;

    @NotBlank(message = "El departamento no puede estar vacío")
    private String departamento;

    @NotBlank(message = "La provincia no puede estar vacía")
    private String provincia;

    @NotBlank(message = "El distrito no puede estar vacío")
    private String distrito;

    @NotBlank(message = "La descripción de los hechos no puede estar vacía")
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcionHechos;

    private List<String> rutasArchivos;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Formato de correo electrónico inválido")
    private String correo;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(LocalDate fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public LocalTime getHoraIncidente() {
        return horaIncidente;
    }

    public void setHoraIncidente(LocalTime horaIncidente) {
        this.horaIncidente = horaIncidente;
    }

    public boolean isEsAhora() {
        return esAhora;
    }

    public void setEsAhora(boolean esAhora) {
        this.esAhora = esAhora;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDescripcionHechos() {
        return descripcionHechos;
    }

    public void setDescripcionHechos(String descripcionHechos) {
        this.descripcionHechos = descripcionHechos;
    }

    public List<String> getRutasArchivos() {
        return rutasArchivos;
    }

    public void setRutasArchivos(List<String> rutasArchivos) {
        this.rutasArchivos = rutasArchivos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
