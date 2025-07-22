package com.themis.themis_backend.dto;

import com.themis.themis_backend.model.Rol;
import java.util.Set;
import java.util.stream.Collectors;

public class UsuarioResponseDTO {

    private Long idUsuario;
    private String nombreUsuario;
    private String correoElectronico;
    private boolean habilitado;
    private Set<String> roles;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long idUsuario, String nombreUsuario, String correoElectronico, boolean habilitado, Set<Rol> rolesEnum) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correoElectronico = correoElectronico;
        this.habilitado = habilitado;
        this.roles = rolesEnum.stream()
                .map(rol -> "ROLE_" + rol.name())
                .collect(Collectors.toSet());
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
