
package com.themis.themis_backend.controller.admin;

import com.themis.themis_backend.dto.UsuarioResponseDTO;
import com.themis.themis_backend.model.Rol;
import com.themis.themis_backend.service.UsuarioServiceImpl;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Nicole
 */
@RestController
@RequestMapping("/api/admin/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminUsuarioController {
    private final UsuarioServiceImpl usuarioService;

    public AdminUsuarioController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        // Necesitarás un método en tu servicio para convertir List<Usuario> a List<UsuarioDTO>
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos().stream()
                                    .map(this::convertToDto) // Método de ayuda para conversión
                                    .toList();
        return ResponseEntity.ok(usuarios);
    }
    
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstadoHabilitado(@PathVariable Long id, @RequestParam boolean habilitado) {
        UsuarioResponseDTO updatedUser = convertToDto(usuarioService.cambiarHabilitado(id, habilitado));
        return ResponseEntity.ok(updatedUser);
    }
    
    @PutMapping("/{id}/roles")
    public ResponseEntity<UsuarioResponseDTO> asignarRoles(@PathVariable Long id, @RequestBody Set<Rol> roles) {
        UsuarioResponseDTO updatedUser = convertToDto(usuarioService.asignarRolesAUsuario(id, roles));
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    
    private UsuarioResponseDTO convertToDto(com.themis.themis_backend.model.Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getCorreoElectronico(),
                usuario.isHabilitado(),
                usuario.getRoles()
        );
    }
}
