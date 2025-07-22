package com.themis.themis_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, 
        include = JsonTypeInfo.As.PROPERTY, 
        property = "denunciaTipo" 
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DenunciaAnonimaResponseDTO.class, name = "ANONIMA"), 
    @JsonSubTypes.Type(value = DenunciaPersonaRealResponseDTO.class, name = "REAL") 
})
// -->>
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DenunciaResponseDTO {

    private Long id;
    private String codigoDenuncia;
    private String estado;
    private LocalDate fechaIncidente;
    private LocalTime horaIncidente;
    private boolean esAhora;
    private String departamento;
    private String provincia;
    private String distrito;
    private String descripcionHechos;
    private List<String> rutasArchivos;
    private LocalDateTime fechaRegistro;
    private boolean anonimo;

    public DenunciaResponseDTO() {
    }
    
    public DenunciaResponseDTO(Long id, String codigoDenuncia, String estado, LocalDate fechaIncidente, LocalTime horaIncidente, boolean esAhora, String departamento, String provincia, String distrito, String descripcionHechos, List<String> rutasArchivos, LocalDateTime fechaRegistro, boolean anonimo) {
        this.id = id;
        this.codigoDenuncia = codigoDenuncia;
        this.estado = estado;
        this.fechaIncidente = fechaIncidente;
        this.horaIncidente = horaIncidente;
        this.esAhora = esAhora;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.descripcionHechos = descripcionHechos;
        this.rutasArchivos = rutasArchivos;
        this.fechaRegistro = fechaRegistro;
        this.anonimo = anonimo;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoDenuncia() {
        return codigoDenuncia;
    }

    public void setCodigoDenuncia(String codigoDenuncia) {
        this.codigoDenuncia = codigoDenuncia;
    }

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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public void setAnonimo(boolean anonimo) {
        this.anonimo = anonimo;
    }

}
