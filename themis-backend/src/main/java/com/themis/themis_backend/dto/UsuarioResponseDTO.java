package com.themis.themis_backend.dto;

import com.themis.themis_backend.model.Rol;
import java.util.Set;

public class UsuarioResponseDTO {

    private Long idUsuario;
    private String nombreUsuario;
    private String correoElectronico;
    private boolean habilitado;
    private Set<Rol> roles;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long idUsuario, String nombreUsuario, String correoElectronico, boolean habilitado, Set<Rol> roles) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correoElectronico = correoElectronico;
        this.habilitado = habilitado;
        this.roles = roles;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

}
