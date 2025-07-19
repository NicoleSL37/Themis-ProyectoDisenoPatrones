package com.themis.themis_backend.controller;

import com.themis.themis_backend.model.DenunciaAnonima;
import com.themis.themis_backend.service.DenunciaAnonimaService;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
@RequestMapping("/api/denuncias-anonimas")
public class DenunciaAnonimaController {

    private final DenunciaAnonimaService denunciaAnonimaService;

    @Autowired
    public DenunciaAnonimaController(DenunciaAnonimaService denunciaAnonimaService) {
        this.denunciaAnonimaService = denunciaAnonimaService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> crearDenunciaAnonima(
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam("fechaIncidente") String fechaIncidente,
            @RequestParam("horaIncidente") String horaIncidente,
            @RequestParam("ahora") boolean ahora,
            @RequestParam("departamento") String departamento,
            @RequestParam("provincia") String provincia,
            @RequestParam("distrito") String distrito,
            @RequestParam("descripcionHechos") String descripcionHechos,
            @RequestParam(value = "archivos", required = false) List<MultipartFile> archivos
    ) {

        System.out.println("\n--- INICIO crearDenunciaAnonima (Controller) ---");
        System.out.println("DEBUG CONTROLLER: Cantidad de archivos recibidos: " + (archivos != null ? archivos.size() : 0));
        if (archivos != null && !archivos.isEmpty()) {
            archivos.forEach(file -> System.out.println("DEBUG CONTROLLER: Archivo recibido - Nombre: " + file.getOriginalFilename() + ", Tamaño: " + file.getSize()));
        }

        try {

            DenunciaAnonima denuncia = new DenunciaAnonima();

            denuncia.setCorreo(correo);
            denuncia.setEsAhora(ahora);
            denuncia.setDepartamento(departamento);
            denuncia.setProvincia(provincia);
            denuncia.setDistrito(distrito);
            denuncia.setDescripcionHechos(descripcionHechos);
            denuncia.setEstado("PENDIENTE");
            DenunciaAnonima savedDenuncia = denunciaAnonimaService.guardarDenuncia(denuncia, archivos);

            System.out.println("DEBUG CONTROLLER: Denuncia guardada con código: " + savedDenuncia.getCodigoDenuncia());
            System.out.println("DEBUG CONTROLLER: Rutas de archivos guardadas en el objeto: " + savedDenuncia.getRutasArchivos());
            System.out.println("--- FIN crearDenunciaAnonima (Controller) ---\n");

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message",
                    "Denuncia anónima registrada con éxito.", "codigoDenuncia", savedDenuncia.getCodigoDenuncia()));
        } catch (IOException e) {
            System.err.println("Error al guardar archivos de la denuncia: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error al procesar los archivos adjuntos: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error al registrar denuncia: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error al registrar la denuncia: " + e.getMessage()));
        }
    }

    @GetMapping("/consultar")
    public ResponseEntity<DenunciaAnonima> consultarDenunciaAnonima(@RequestParam String codigoDenuncia) {
        Optional<DenunciaAnonima> denuncia = denunciaAnonimaService.findByCodigoDenuncia(codigoDenuncia);
        return denuncia.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        System.err.println("ERROR GLOBAL: MethodArgumentTypeMismatchException en DenunciaAnonimaController: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La URL del archivo anónimo es inválida o contiene caracteres no permitidos."));
    }
}
