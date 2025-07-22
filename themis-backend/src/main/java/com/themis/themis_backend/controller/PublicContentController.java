
package com.themis.themis_backend.controller;

import com.themis.themis_backend.dto.ContenidoDTO;
import com.themis.themis_backend.service.ContenidoService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Nicole
 */

@RestController
@RequestMapping("/api/contenidos/public")
public class PublicContentController {
    private final ContenidoService contenidoService;

    public PublicContentController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }
    
    @GetMapping
    public ResponseEntity<List<ContenidoDTO>> getAllPublicContent(){
        List<ContenidoDTO> contenidos = contenidoService.findAllPublicContents();
        return ResponseEntity.ok(contenidos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ContenidoDTO> getPublicContentById(@PathVariable Long id){
        Optional<ContenidoDTO> contenido = contenidoService.findPublicContentById(id);
        return contenido.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
