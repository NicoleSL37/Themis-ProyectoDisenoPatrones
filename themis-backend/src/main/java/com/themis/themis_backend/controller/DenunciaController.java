package com.themis.themis_backend.controller;

import com.themis.themis_backend.dto.DenunciaAnonimaRequestDTO;
import com.themis.themis_backend.dto.DenunciaPersonaRealRequestDTO;
import com.themis.themis_backend.dto.DenunciaResponseDTO; // Importa la clase base del DTO de respuesta
import com.themis.themis_backend.dto.DenunciaAnonimaResponseDTO; // Importa los DTOs específicos de respuesta
import com.themis.themis_backend.dto.DenunciaPersonaRealResponseDTO; // Importa los DTOs específicos de respuesta
import com.themis.themis_backend.exception.ResourceNotFoundException;

import com.themis.themis_backend.model.Denuncia;
import com.themis.themis_backend.model.DenunciaAnonima;
import com.themis.themis_backend.model.DenunciaPersonaReal;
import com.themis.themis_backend.model.Usuario;
import com.themis.themis_backend.service.IDenunciaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Para la seguridad
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    private final IDenunciaService denunciaService;

    public DenunciaController(IDenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    // <<-- Métodos de mapeo de Entidad a DTO de Respuesta -->>
    private DenunciaResponseDTO toResponseDTO(Denuncia denuncia) {
        if (denuncia instanceof DenunciaAnonima) {
            DenunciaAnonima anonima = (DenunciaAnonima) denuncia;
            DenunciaAnonimaResponseDTO dto = new DenunciaAnonimaResponseDTO();
            mapCommonFields(denuncia, dto);
            dto.setCorreo(anonima.getCorreo());
            return dto;
        } else if (denuncia instanceof DenunciaPersonaReal) {
            DenunciaPersonaReal real = (DenunciaPersonaReal) denuncia;
            DenunciaPersonaRealResponseDTO dto = new DenunciaPersonaRealResponseDTO();
            mapCommonFields(denuncia, dto);
            dto.setNombres(real.getNombres());
            dto.setApellidoPaterno(real.getApellidoPaterno());
            dto.setApellidoMaterno(real.getApellidoMaterno());
            dto.setTipoDocumento(real.getTipoDocumento());
            dto.setNumeroDocumento(real.getNumeroDocumento());
            dto.setSexo(real.getSexo());
            dto.setFechaNacimiento(real.getFechaNacimiento());
            dto.setFechaEmision(real.getFechaEmision());
            dto.setNumeroCelular(real.getNumeroCelular());
            dto.setCorreoElectronico(real.getCorreoElectronico());
            dto.setAutorizoDatos(real.isAutorizoDatos());
            return dto;
        }
        // Considera lanzar una excepción si llega un tipo no reconocido, aunque en herencia es menos común.
        throw new IllegalArgumentException("Tipo de denuncia no reconocido para mapeo a DTO de respuesta.");
    }

    // Helper para mapear campos comunes (Este método permanece igual)
    private void mapCommonFields(Denuncia source, DenunciaResponseDTO target) {
        target.setId(source.getId());
        target.setCodigoDenuncia(source.getCodigoDenuncia());
        target.setEstado(source.getEstado());
        target.setFechaIncidente(source.getFechaIncidente());
        target.setHoraIncidente(source.getHoraIncidente());
        target.setEsAhora(source.isEsAhora());
        target.setDepartamento(source.getDepartamento());
        target.setProvincia(source.getProvincia());
        target.setDistrito(source.getDistrito());
        target.setDescripcionHechos(source.getDescripcionHechos());
        target.setRutasArchivos(source.getRutasArchivos());
        target.setFechaRegistro(source.getFechaRegistro());
        target.setAnonimo(source.isAnonimo());
    }
    // -->>

    // Endpoint para crear una Denuncia Anónima
    @PostMapping("/anonima")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public ResponseEntity<DenunciaResponseDTO> crearDenunciaAnonima(@Valid @RequestBody DenunciaAnonimaRequestDTO requestDTO) {
        DenunciaAnonima denuncia = new DenunciaAnonima();
        mapRequestDTOToDenuncia(requestDTO, denuncia);
        denuncia.setCorreo(requestDTO.getCorreo());

        // <-- Elimina el try-catch de RuntimeException, el GlobalExceptionHandler se encargará
        DenunciaAnonima nuevaDenuncia = denunciaService.crearDenunciaAnonima(denuncia);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(nuevaDenuncia));
    }

    // Endpoint para crear una Denuncia de Persona Real
    @PostMapping("/real")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USUARIO_REGISTRADO') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<DenunciaResponseDTO> crearDenunciaPersonaReal(
            @Valid @RequestBody DenunciaPersonaRealRequestDTO requestDTO,
            Authentication authentication) {

        DenunciaPersonaReal denuncia = new DenunciaPersonaReal();
        mapRequestDTOToDenuncia(requestDTO, denuncia);

        denuncia.setNombres(requestDTO.getNombres());
        denuncia.setApellidoPaterno(requestDTO.getApellidoPaterno());
        denuncia.setApellidoMaterno(requestDTO.getApellidoMaterno());
        denuncia.setTipoDocumento(requestDTO.getTipoDocumento());
        denuncia.setNumeroDocumento(requestDTO.getNumeroDocumento());
        denuncia.setSexo(requestDTO.getSexo());
        denuncia.setFechaNacimiento(requestDTO.getFechaNacimiento());
        denuncia.setFechaEmision(requestDTO.getFechaEmision());
        denuncia.setNumeroCelular(requestDTO.getNumeroCelular());
        denuncia.setCorreoElectronico(requestDTO.getCorreoElectronico());
        denuncia.setAutorizoDatos(requestDTO.isAutorizoDatos());

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Usuario) { 
                denuncia.setUsuario((Usuario) principal);
            } 
        }


        DenunciaPersonaReal nuevaDenuncia = denunciaService.crearDenunciaPersonaReal(denuncia);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(nuevaDenuncia));
    }

    private void mapRequestDTOToDenuncia(Object requestDTO, Denuncia denuncia) {
        if (requestDTO instanceof DenunciaAnonimaRequestDTO) {
            DenunciaAnonimaRequestDTO dto = (DenunciaAnonimaRequestDTO) requestDTO;
            denuncia.setEstado(dto.getEstado());
            denuncia.setFechaIncidente(dto.getFechaIncidente());
            denuncia.setHoraIncidente(dto.getHoraIncidente());
            denuncia.setEsAhora(dto.isEsAhora());
            denuncia.setDepartamento(dto.getDepartamento());
            denuncia.setProvincia(dto.getProvincia());
            denuncia.setDistrito(dto.getDistrito());
            denuncia.setDescripcionHechos(dto.getDescripcionHechos());
            denuncia.setRutasArchivos(dto.getRutasArchivos());
            denuncia.setAnonimo(true); // Una denuncia anónima siempre es anónima
        } else if (requestDTO instanceof DenunciaPersonaRealRequestDTO) {
            DenunciaPersonaRealRequestDTO dto = (DenunciaPersonaRealRequestDTO) requestDTO;
            denuncia.setEstado(dto.getEstado());
            denuncia.setFechaIncidente(dto.getFechaIncidente());
            denuncia.setHoraIncidente(dto.getHoraIncidente());
            denuncia.setEsAhora(dto.isEsAhora());
            denuncia.setDepartamento(dto.getDepartamento());
            denuncia.setProvincia(dto.getProvincia());
            denuncia.setDistrito(dto.getDistrito());
            denuncia.setDescripcionHechos(dto.getDescripcionHechos());
            denuncia.setRutasArchivos(dto.getRutasArchivos());
            denuncia.setAnonimo(false); // Una denuncia de persona real no es anónima
        }
    }

    // Endpoint para obtener una denuncia por ID (puede ser anónima o real)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or (hasRole('USUARIO_REGISTRADO') and @denunciaSecurity.isOwner(#id, authentication.principal.idUsuario))")
    public ResponseEntity<DenunciaResponseDTO> obtenerDenunciaPorId(@PathVariable Long id) {
        Denuncia denuncia = denunciaService.obtenerDenunciaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia no encontrada con ID: " + id)); // <-- CAMBIO AQUÍ
        return ResponseEntity.ok(toResponseDTO(denuncia));
    }

    // Endpoint para listar todas las denuncias (solo para ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<DenunciaResponseDTO>> listarTodasLasDenuncias() {
        List<Denuncia> denuncias = denunciaService.listarTodasLasDenuncias();
        List<DenunciaResponseDTO> dtoList = denuncias.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint para listar denuncias anónimas (solo para ADMIN)
    @GetMapping("/anonimas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<DenunciaResponseDTO>> listarDenunciasAnonimas() {
        List<Denuncia> denuncias = denunciaService.listarDenunciasAnonimas();
        List<DenunciaResponseDTO> dtoList = denuncias.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint para listar denuncias de persona real (solo para ADMIN)
    @GetMapping("/reales")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<DenunciaResponseDTO>> listarDenunciasPersonaReal() {
        List<Denuncia> denuncias = denunciaService.listarDenunciasPersonaReal();
        List<DenunciaResponseDTO> dtoList = denuncias.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint para actualizar el estado de una denuncia (solo para ADMIN)
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<DenunciaResponseDTO> actualizarEstadoDenuncia(@PathVariable Long id, @RequestParam String nuevoEstado) {
        // <<-- Elimina el try-catch. Asume que el servicio lanzará ResourceNotFoundException -->>
        Denuncia denunciaActualizada = denunciaService.actualizarEstadoDenuncia(id, nuevoEstado);
        return ResponseEntity.ok(toResponseDTO(denunciaActualizada));
    }

    // Endpoint para eliminar una denuncia (solo para ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void eliminarDenuncia(@PathVariable Long id) {
        // <<-- Elimina el try-catch. Asume que el servicio lanzará ResourceNotFoundException -->>
        denunciaService.eliminarDenuncia(id);
    }

}
