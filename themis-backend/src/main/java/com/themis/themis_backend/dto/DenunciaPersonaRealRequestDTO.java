package com.themis.themis_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DenunciaPersonaRealRequestDTO {

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

    private List<String> rutasArchivos; // Opcional, puede ser null

    // Campos específicos de DenunciaPersonaReal
    @NotBlank(message = "Los nombres no pueden estar vacíos")
    private String nombres;

    @NotBlank(message = "El apellido paterno no puede estar vacío")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno no puede estar vacío")
    private String apellidoMaterno;

    @NotBlank(message = "El tipo de documento no puede estar vacío")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento no puede estar vacío")
    private String numeroDocumento;

    @NotBlank(message = "El sexo no puede estar vacío")
    private String sexo;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fechaNacimiento;

    private LocalDate fechaEmision; // Opcional

    private String numeroCelular; // Opcional

    @Email(message = "Formato de correo electrónico inválido")
    private String correoElectronico; // Opcional

    private boolean autorizoDatos;

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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public boolean isAutorizoDatos() {
        return autorizoDatos;
    }

    public void setAutorizoDatos(boolean autorizoDatos) {
        this.autorizoDatos = autorizoDatos;
    }

}
