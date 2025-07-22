
package com.themis.themis_backend.controller.admin;

import com.themis.themis_backend.dto.ContenidoDTO;
import com.themis.themis_backend.service.ContenidoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author Nicole
 */
@RestController
@RequestMapping("/api/admin/contenidos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminContenidoController {
    
    private final ContenidoService contenidoService;

    public AdminContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }
    
    @PostMapping
    public ResponseEntity<ContenidoDTO> createContenido(@RequestBody ContenidoDTO contenidoDTO){
        ContenidoDTO nuevoContenido = contenidoService.createContenido(contenidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContenido);
    }
    
    @GetMapping
    public ResponseEntity<List<ContenidoDTO>> getAllContenidos(){
        List<ContenidoDTO> contenidos = contenidoService.findAllPublicContents();
        return ResponseEntity.ok(contenidos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ContenidoDTO> getContenidoById(@PathVariable Long id){
        return contenidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ContenidoDTO> updateContenido(@PathVariable Long id, @RequestBody ContenidoDTO contenidoDTO){
        ContenidoDTO updateContenido = contenidoService.updateContenido(id, contenidoDTO);
        return ResponseEntity.ok(updateContenido);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenido(@PathVariable Long id){
        contenidoService.deleteContenido(id);
        return ResponseEntity.noContent().build();
    }
}
