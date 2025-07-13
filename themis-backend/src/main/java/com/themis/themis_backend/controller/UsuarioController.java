package com.themis.themis_backend.controller;

import com.themis.themis_backend.dto.UsuarioRegistroDTO;
import com.themis.themis_backend.dto.UsuarioResponseDTO;
import com.themis.themis_backend.model.Rol;
import com.themis.themis_backend.model.Usuario;
import com.themis.themis_backend.service.IUsuarioService;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getCorreoElectronico(),
                usuario.isHabilitado(),
                usuario.getRoles()
        );
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO registroDTO) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(registroDTO.getNombreUsuario());
        nuevoUsuario.setContrasenia(registroDTO.getContrasenia());
        nuevoUsuario.setCorreoElectronico(registroDTO.getCorreoElectronico());
        if (registroDTO.getRoles() != null && !registroDTO.getRoles().isEmpty()) {
            nuevoUsuario.setRoles(registroDTO.getRoles());
        } else {

        }
        try {
            Usuario usuarioRegistrado = usuarioService.registrarUsuario(nuevoUsuario);
            return new ResponseEntity<>(toResponseDTO(usuarioRegistrado), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or (#id == authentication.principal.idUsuario)")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        return usuario.map(this::toResponseDTO).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or (#id == authentication.principal.idUsuario)")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRegistroDTO usuarioActualizadoDTO) {
        Usuario usuarioParaActualizar = new Usuario();
        usuarioParaActualizar.setIdUsuario(id);
        usuarioParaActualizar.setNombreUsuario(usuarioActualizadoDTO.getNombreUsuario());
        usuarioParaActualizar.setCorreoElectronico(usuarioActualizadoDTO.getCorreoElectronico());

        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuarioParaActualizar);
            return ResponseEntity.ok(toResponseDTO(usuarioActualizado));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}/habilitar/{estado}")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstadoHabilitado(@PathVariable Long id, @PathVariable boolean estado) {
        try {
            Usuario usuario = usuarioService.cambiarHabilitado(id, estado);
            return ResponseEntity.ok(toResponseDTO(usuario));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}/roles")
    public ResponseEntity<UsuarioResponseDTO> asignarRolesAUsuario(@PathVariable Long id, @RequestBody Set<Rol> roles) {
        try {
            Usuario usuario = usuarioService.asignarRolesAUsuario(id, roles);
            return ResponseEntity.ok(toResponseDTO(usuario));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
