
package com.themis.themis_backend.controller;

import com.themis.themis_backend.service.DenunciaAnonimaService;
import com.themis.themis_backend.service.DenunciaPersonaRealService;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.*;


import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class DenunciaArchivoController {
    private final DenunciaPersonaRealService denunciaPersonaRealService;
    private final DenunciaAnonimaService denunciaAnonimaService;

    public DenunciaArchivoController(DenunciaPersonaRealService denunciaPersonaRealService, DenunciaAnonimaService denunciaAnonimaService) {
        this.denunciaPersonaRealService = denunciaPersonaRealService;
        this.denunciaAnonimaService = denunciaAnonimaService;
    }
    
    @GetMapping("/api/denuncias/descargar-archivos/{*filePath}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filePath){
        System.out.println("DEBUG CONTROLLER: Solicitud de descarga recibida para el archivo: " + filePath);
        
        Resource resource;
        try{
            resource = denunciaPersonaRealService.loadFileAsResource(filePath);
            
            String contentType = null;
            try{
                Path fileActualPath = Paths.get(resource.getURI());
                contentType = Files.probeContentType(fileActualPath);
            } catch(IOException ex){
                System.err.println("ERROR CONTROLLER: No se pudo determinar el tipo de contenido para " + filePath);
            }
            if(contentType == null){
                contentType = "application/octet-stream";
            }
            String headerValue;
            if(contentType.equals("application/pdf") || contentType.startsWith("image/")){
                headerValue = "inline; filename=\"" + resource.getFilename() + "\"";
                System.out.println("DEBUG CONTROLLER: Archivo identificado como PDF/Imagen. Sugerencia: INLINE.");
            } else{
                headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
                System.out.println("DEBUG CONTROLLER: Archivo identificado como otro tipo. Sugerencia: ATTACHMENT.");
            }
            
            System.out.println("DEBUG CONTROLLER: Archivo " + filePath + "' listo para servir. Content-Type: " + contentType);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        } catch(FileNotFoundException ex){
            System.err.println("ERROR CONTROLLER: Error inesperado al descargar " + filePath + ": " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
            
        } catch(Exception ex){
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor al descargar el archivo: " + filePath, ex);
        }
    }
    
    @GetMapping("/api/denuncias-anonimas/archivo/{*filePath}")
    public ResponseEntity<Resource> downloadAnonymousFile(@PathVariable String filePath){
        System.out.println("DEBUG coNTROLLER: Solicitud de descarga (Anónima) recibida para el archivo: " + filePath);
        
        Resource resource;
        try{
            resource = denunciaAnonimaService.loadFileAsResource(filePath);
            
            String contentType = null;
            try{
                Path fileActualPath = Paths.get(resource.getURI());
                contentType = Files.probeContentType(fileActualPath);
            } catch(IOException ex){
                System.err.println("ERROR CONTROLLER: No se pudo determinar el tipo de contenido para archivo anónimo " + filePath);
            }
            if(contentType == null){
                contentType = "application/octet-stream";
            }
            String headerValue;
            if(contentType.equals("application/pdf") || contentType.startsWith("image/")){
                headerValue = "inline; filename=\"" + resource.getFilename() + "\"";
                System.out.println("DEBUG CONTROLLER: Archivo anónimo identificado como PDF/Imagen. Sugerencia: INLINE");
            } else{
                headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
                System.out.println("DEBUG CONTROLLER: Archivo anónimo identificado como otro tipo. Sugerencia: ATTACHMENT");
            }
            System.out.println("DEBUG CONTROLLER: Archivo anónimo '" + filePath + "' listo para servir. Content-Type: " + contentType);
             return ResponseEntity.ok()
                     .contentType(MediaType.parseMediaType(contentType))
                     .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                     .body(resource);
        } catch(FileNotFoundException ex){
            System.err.println("ERROR CONTROLLER: Archivo anónimo no encontrado " + filePath + ": " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }catch(Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor al descargar el archivo anónimo: " + filePath, ex);
        }
    }
}
