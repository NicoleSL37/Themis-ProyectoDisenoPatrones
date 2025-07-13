package com.themis.themis_backend.controller;

import com.themis.themis_backend.model.DenunciaAnonima;
import com.themis.themis_backend.model.DenunciaPersonaReal;
import com.themis.themis_backend.service.DenunciaAnonimaService;
import com.themis.themis_backend.service.DenunciaPersonaRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import java.util.Optional;

@RestController
@RequestMapping("/api/consultar-denuncia")
public class ConsultaDenunciaController {

    @Autowired
    private DenunciaAnonimaService denunciaAnonimaService;

    @Autowired
    private DenunciaPersonaRealService denunciaPersonaRealService;

    @GetMapping
    public ResponseEntity<?> consultarDenuncia(
            @RequestParam(required = true) String tipoDenuncia,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String numeroDocumento,
            @RequestParam(required = true) String codigoDenuncia) {

        String tipoDenunciaLowerCase = tipoDenuncia.toLowerCase();

        if (codigoDenuncia == null || codigoDenuncia.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "El código de denuncia es obligatorio."));
        }

        if ("anonima".equals(tipoDenunciaLowerCase)) {

            Optional<DenunciaAnonima> denuncia = denunciaAnonimaService.findByCodigoDenuncia(codigoDenuncia);
            if (denuncia.isPresent()) {
                return ResponseEntity.ok(denuncia.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "Denuncia anónima no encontrada con el código proporcionado."));
            }
        } else if ("personareal".equals(tipoDenunciaLowerCase)) {
            if (tipoDocumento == null || tipoDocumento.isEmpty() || numeroDocumento == null || numeroDocumento.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Para denuncias de persona real, el tipo y número de documento son obligatorios."));
            }

            Optional<DenunciaPersonaReal> denuncia = denunciaPersonaRealService.findByCodigoDenunciaAndTipoDocumentoAndNumeroDocumento(
                    codigoDenuncia, tipoDocumento, numeroDocumento
            );

            if (denuncia.isPresent()) {
                return ResponseEntity.ok(denuncia.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "Denuncia de persona real no encontrada con los datos proporcionados."));
            }
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Tipo de denuncia no válido. Debe ser 'anonima' o 'personaReal'."));
        }
    }
}
