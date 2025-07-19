package com.themis.themis_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "denuncias_persona_real_detalles")
@PrimaryKeyJoinColumn(name = "denuncia_id")
public class DenunciaPersonaReal extends Denuncia {

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false)
    private String numeroDocumento;

    @Column(name = "sexo", nullable = false)
    private String sexo;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "numero_celular")
    private String numeroCelular;

    @Column(name = "correo_electronico_denunciante") // Renombrado para evitar conflicto con DenunciaAnonima.correo si se usara el mismo nombre
    private String correoElectronico;

    @Column(name = "autorizo_datos", nullable = false)
    private boolean autorizoDatos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true) // Puede ser nullable si una denuncia real no requiere un usuario logueado
    private Usuario usuario;

    // <<-- Constructor vacío -->>
    public DenunciaPersonaReal() {
        super();
        // Puedes setear valores por defecto específicos aquí si no quieres que el frontend lo haga
    }

    @Override
    @PrePersist
    public void generateCodigoAndSetFechaRegistro() {
        super.generateCodigoAndSetFechaRegistro(); // Llama al método de la superclase primero
        if (!this.codigoDenuncia.startsWith("REAL-")) { // Solo añade el prefijo si no lo tiene ya
            this.codigoDenuncia = "REAL-" + this.codigoDenuncia;
        }
        this.setAnonimo(false); // Asegura que 'anonimo' siempre sea false para este tipo
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
