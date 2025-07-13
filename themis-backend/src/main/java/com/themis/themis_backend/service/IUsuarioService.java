package com.themis.themis_backend.service;

import com.themis.themis_backend.model.Rol;
import com.themis.themis_backend.model.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUsuarioService {

    Usuario registrarUsuario(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);

    Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico);

    List<Usuario> listarTodos();

    Usuario actualizarUsuario(Usuario usuario);

    Usuario cambiarHabilitado(Long idUsuario, boolean habilitado);

    Usuario asignarRolesAUsuario(Long idUsuario, Set<Rol> nuevosRoles);

    void eliminarUsuario(Long id);
}
