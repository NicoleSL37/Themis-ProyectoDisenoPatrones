package com.themis.themis_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.themis.themis_backend.dto.DenunciaPersonaRealRequestDTO;
import com.themis.themis_backend.model.DenunciaPersonaReal;
import com.themis.themis_backend.service.DenunciaPersonaRealService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;

import org.springframework.http.MediaType;

import java.nio.file.Files;

import java.nio.file.Paths;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController // Indica que es un controlador REST
@RequestMapping("/api/denuncias-personas-reales") // Define la URL base para este controlador
public class DenunciaPersonaRealController {

    private final DenunciaPersonaRealService denunciaPersonaRealService;
    private final ObjectMapper objectMapper;

    @Autowired
    public DenunciaPersonaRealController(DenunciaPersonaRealService denunciaPersonaRealService, ObjectMapper objectMapper) {
        this.denunciaPersonaRealService = denunciaPersonaRealService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearDenunciaPersonaReal(
            @RequestParam("denuncia") String denunciaJson,
            @RequestParam(value = "archivosEvidencia", required = false) List<MultipartFile> archivos
    ) {
        try {
            
            DenunciaPersonaRealRequestDTO denunciaDto = objectMapper.readValue(denunciaJson, DenunciaPersonaRealRequestDTO.class);
            DenunciaPersonaReal denuncia = new DenunciaPersonaReal();

            // Asignación de datos personales
            denuncia.setNombres(denunciaDto.getNombres());
            denuncia.setApellidoPaterno(denunciaDto.getApellidoPaterno());
            denuncia.setApellidoMaterno(denunciaDto.getApellidoMaterno());
            denuncia.setTipoDocumento(denunciaDto.getTipoDocumento());
            denuncia.setNumeroDocumento(denunciaDto.getNumeroDocumento());
            denuncia.setSexo(denunciaDto.getSexo());

            denuncia.setFechaNacimiento(denunciaDto.getFechaNacimiento());
            denuncia.setFechaEmision(denunciaDto.getFechaEmision());
            denuncia.setNumeroCelular(denunciaDto.getNumeroCelular());
            denuncia.setCorreoElectronico(denunciaDto.getCorreoElectronico());
            denuncia.setAutorizoDatos(denunciaDto.isAutorizoDatos());

            // Asignación de datos del incidente
            denuncia.setEsAhora(denunciaDto.isEsAhora());
            denuncia.setFechaIncidente(denunciaDto.getFechaIncidente());
            denuncia.setHoraIncidente(denunciaDto.getHoraIncidente());
            
            denuncia.setDepartamento(denunciaDto.getDepartamento());
            denuncia.setProvincia(denunciaDto.getProvincia());
            denuncia.setDistrito(denunciaDto.getDistrito());
            denuncia.setEstado("PENDIENTE");
            denuncia.setDescripcionHechos(denunciaDto.getDescripcionHechos());

            DenunciaPersonaReal denunciaGuardada = denunciaPersonaRealService.guardarDenuncia(denuncia, archivos);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Denuncia de persona real registrada con éxito.", "codigoDenuncia", denunciaGuardada.getCodigoDenuncia()));
        } catch (IOException e) {
            System.err.println("Error al procesar JSPN o guardar archivos de la denuncia de persona real: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error al procesar los archivos adjuntos: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado al crear denuncia de persona real: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error al crear denuncia: " + e.getMessage() + ". Por favor, revise los datos enviados."));
        }
    }

    @GetMapping("/archivo/{*rutaRelativaArchivo}")
    public ResponseEntity<Resource> servirArchivoAdjunto(HttpServletRequest request, @PathVariable String rutaRelativaArchivo) {
        try {
            System.out.println("\n--- INICIO servirArchivoAdjunto (Controller - Última Versión - Intentando @PathVariable)) ---"); // DEBUG
            System.out.println("DEBUG CONTROLLER: rutaRelativaArchivo (de @PathVariable): " + rutaRelativaArchivo); // DEBUG
            // Asumiendo que loadFileAsResource está en tu DenunciaPersonaRealService

            if (rutaRelativaArchivo == null || rutaRelativaArchivo.isEmpty()) {
                System.err.println("ERROR CONTROLLER: rutaRelativaArchivo es nula o vacía.");
                return ResponseEntity.badRequest().body(null);
            }
            Resource resource = denunciaPersonaRealService.loadFileAsResource(rutaRelativaArchivo);

            if (resource.exists() || resource.isReadable()) {
                String contentType = null;
                try {
                    contentType = Files.probeContentType(Paths.get(resource.getURI()));
                } catch (IOException ex) {
                    System.err.println("No se pudo determinar el tipo de contenido para el archivo: " + rutaRelativaArchivo);
                }
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Tipo por defecto si no se puede determinar
                }

                System.out.println("--- FIN servirArchivoAdjunto (Controller - ÉXITO) ---\n");
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        // Usar Content-Disposition "inline" para visualizar en el navegador, "attachment" para descargar
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Archivo no encontrado o no se puede leer
                System.err.println("ERROR CONTROLLER: Archivo NO encontrado o NO legible en la ruta: " + rutaRelativaArchivo);
                return ResponseEntity.notFound().build();
            }
        } catch (FileNotFoundException ex) {
            System.err.println("ERROR CONTROLLER: Archivo no encontrado (FileNotFoundException): " + ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) { // Captura cualquier otra excepción inesperada
            System.err.println("ERROR CONTROLLER: Error inesperado al servir el archivo: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        System.err.println("ERROR GLOBAL: MethodArgumentTypeMismatchException: " + ex.getMessage());
        System.err.println("ERROR GLOBAL: Parámetro con problema: " + ex.getName() + ", Valor: " + ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La URL del archivo es inválida o contiene caracteres no permitidos."));
    }

}
