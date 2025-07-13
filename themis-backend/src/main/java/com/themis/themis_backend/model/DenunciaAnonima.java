package com.themis.themis_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.PrePersist;

import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("ANONIMA")
public class DenunciaAnonima extends Denuncia {

    @Column(name = "correo_anonima") // Para diferenciar del campo de persona real si existiera
    private String correo; // Correo de contacto opcional para la denuncia anónima

    // <<-- Constructor vacío -->>
    public DenunciaAnonima() {
        super();
        // Puedes setear valores por defecto específicos aquí si no quieres que el frontend lo haga
        // Por ejemplo, para asegurarte que el prefijo del código es ANON-
        // setea el campo anonimo a true por defecto
    }

    @Override
    @PrePersist
    public void generateCodigoAndSetFechaRegistro() {
        super.generateCodigoAndSetFechaRegistro(); // Llama al método de la superclase primero
        if (!this.codigoDenuncia.startsWith("ANON-")) { // Solo añade el prefijo si no lo tiene ya
            this.codigoDenuncia = "ANON-" + this.codigoDenuncia;
        }
        this.setAnonimo(true); // Asegura que 'anonimo' siempre sea true para este tipo
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
