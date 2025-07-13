package com.themis.themis_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "categorias_denuncia") // Asegúrate de que el nombre de tu tabla sea correcto
@EntityListeners(AuditingEntityListener.class)
public class CategoriaDenuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Ej: "Violencia de Género", "Robo", "Fraude"

    @Column(columnDefinition = "TEXT")
    private String descripcion; // Descripción opcional de la categoría

    @CreatedDate // <-- Nueva anotación para la fecha de creación
    @Column(nullable = false, updatable = false) // No puede ser nula y no se actualiza manualmente
    private LocalDateTime fechaRegistro;

    @LastModifiedDate // <-- Nueva anotación para la fecha de última modificación
    @Column(insertable = false) // Se actualiza pero no se inserta manualmente
    private LocalDateTime fechaActualizacion; // <-- Nuevo campo

    // Constructor vacío (necesario para JPA)
    public CategoriaDenuncia() {
    }

    public CategoriaDenuncia(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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
