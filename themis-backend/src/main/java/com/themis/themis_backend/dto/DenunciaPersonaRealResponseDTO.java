package com.themis.themis_backend.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@JsonTypeName("REAL")
public class DenunciaPersonaRealResponseDTO extends DenunciaResponseDTO {

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String sexo;
    private LocalDate fechaNacimiento;
    private LocalDate fechaEmision;
    private String numeroCelular;
    private String correoElectronico;
    private boolean autorizoDatos;

    public DenunciaPersonaRealResponseDTO() {
        super();
    }

    
    
    public DenunciaPersonaRealResponseDTO(String nombres, String apellidoPaterno, String apellidoMaterno, String tipoDocumento, String numeroDocumento, String sexo, LocalDate fechaNacimiento, LocalDate fechaEmision, String numeroCelular, String correoElectronico, boolean autorizoDatos, Long id, String codigoDenuncia, String estado, LocalDate fechaIncidente, LocalTime horaIncidente, boolean esAhora, String departamento, String provincia, String distrito, String descripcionHechos, List<String> rutasArchivos, LocalDateTime fechaRegistro, boolean anonimo) {
        super(id, codigoDenuncia, estado, fechaIncidente, horaIncidente, esAhora, departamento, provincia, distrito, descripcionHechos, rutasArchivos, fechaRegistro, anonimo);
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaEmision = fechaEmision;
        this.numeroCelular = numeroCelular;
        this.correoElectronico = correoElectronico;
        this.autorizoDatos = autorizoDatos;
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
