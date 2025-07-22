
package com.themis.themis_backend.dto;

import jakarta.validation.constraints.*;

/**
 *
 * @author Nicole
 */
public class ContenidoDTO {
    private Long id;
    
    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 255, message = "El contenido no puede estar vacío")
    private String titulo;
    
    @NotBlank(message = "El contenido no puede estar vacío")
    private String contenido;
    
    private boolean esPublico;

    public ContenidoDTO(Long id, String titulo, String contenido, boolean esPublico) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.esPublico = esPublico;
    }

    public ContenidoDTO() {
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
    
    
}
