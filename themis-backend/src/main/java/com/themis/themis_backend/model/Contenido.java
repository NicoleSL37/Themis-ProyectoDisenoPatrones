
package com.themis.themis_backend.model;

import jakarta.persistence.*;


@Entity
@Table(name = "contenidos")
public class Contenido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String titulo;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    @Column(name = "es_publico", nullable = false)
    private boolean esPublico;

    public Contenido() {
    }

    public Contenido(String titulo, String contenido, boolean esPublico) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.esPublico = esPublico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEsPublico() {
        return esPublico;
    }

    public void setEsPublico(boolean esPublico) {
        this.esPublico = esPublico;
    }
    
    @Override
    public String toString(){
        return "Contenido{" +
                "id = " + id +
                ", titulo = '" + titulo + '\'' +
                ", esPublico = " + esPublico +
                '}';
    }
}
