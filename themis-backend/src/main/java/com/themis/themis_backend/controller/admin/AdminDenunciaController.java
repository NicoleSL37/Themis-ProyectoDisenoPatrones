
package com.themis.themis_backend.controller.admin;

import com.themis.themis_backend.dto.DenunciaResponseDTO;
import com.themis.themis_backend.model.Denuncia;
import com.themis.themis_backend.service.DenunciaServiceImpl;
import com.themis.themis_backend.service.IDenunciaService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Nicole
 */
@RestController
@RequestMapping("/api/admin/denuncias") // O "/api/denuncias" si tu SecurityConfig ya lo cubre con /api/admin/**
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminDenunciaController {
    private final IDenunciaService denunciaService;

    public AdminDenunciaController(IDenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }
    
    @GetMapping
    public ResponseEntity<List<DenunciaResponseDTO>> getAllDenuncias() {
        List<DenunciaResponseDTO> denuncias = denunciaService.listarTodasLasDenuncias();
        return ResponseEntity.ok(denuncias);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DenunciaResponseDTO> getDenunciaById(@PathVariable Long id) {
        Optional<DenunciaResponseDTO> denuncia = denunciaService.obtenerDenunciaPorId(id);
        return denuncia.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<DenunciaResponseDTO> updateDenunciaStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        String nuevoEstado = statusUpdate.get("estado");
        DenunciaResponseDTO updatedDenuncia = denunciaService.actualizarEstadoDenuncia(id, nuevoEstado);
        return ResponseEntity.ok(updatedDenuncia);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDenuncia(@PathVariable Long id) {
        denunciaService.eliminarDenuncia(id);
        return ResponseEntity.noContent().build();
    }
}
