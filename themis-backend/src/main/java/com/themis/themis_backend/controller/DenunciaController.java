package com.themis.themis_backend.controller;

import com.themis.themis_backend.dto.DenunciaAnonimaRequestDTO;
import com.themis.themis_backend.dto.DenunciaAnonimaResponseDTO;
import com.themis.themis_backend.dto.DenunciaPersonaRealRequestDTO;
import com.themis.themis_backend.dto.DenunciaPersonaRealResponseDTO;
import com.themis.themis_backend.dto.DenunciaResponseDTO; // Importa la clase base del DTO de respuesta

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


    @PostMapping("/anonima")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public ResponseEntity<DenunciaResponseDTO> crearDenunciaAnonima(@Valid @RequestBody DenunciaAnonimaRequestDTO requestDTO) {
        DenunciaAnonima denuncia = new DenunciaAnonima();
        mapRequestDTOToDenuncia(requestDTO, denuncia);

        DenunciaAnonima nuevaDenuncia = denunciaService.crearDenunciaAnonima(denuncia);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.convertEntityToResponseDTO(nuevaDenuncia));
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
        return ResponseEntity.status(HttpStatus.CREATED).body(this.convertEntityToResponseDTO(nuevaDenuncia));
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
    
    private DenunciaResponseDTO convertEntityToResponseDTO(Denuncia denuncia){
        if (denuncia instanceof DenunciaAnonima) {
            DenunciaAnonima anonima = (DenunciaAnonima) denuncia;
            DenunciaAnonimaResponseDTO dto = new DenunciaAnonimaResponseDTO();
            // Mapeo manual o usar un BeanUtils.copyProperties si confías en los nombres de los campos
            dto.setId(anonima.getId());
            dto.setCodigoDenuncia(anonima.getCodigoDenuncia());
            dto.setEstado(anonima.getEstado());
            dto.setFechaIncidente(anonima.getFechaIncidente());
            dto.setHoraIncidente(anonima.getHoraIncidente());
            dto.setEsAhora(anonima.isEsAhora());
            dto.setDepartamento(anonima.getDepartamento());
            dto.setProvincia(anonima.getProvincia());
            dto.setDistrito(anonima.getDistrito());
            dto.setDescripcionHechos(anonima.getDescripcionHechos());
            dto.setRutasArchivos(anonima.getRutasArchivos());
            dto.setFechaRegistro(anonima.getFechaRegistro());
            dto.setAnonimo(anonima.isAnonimo());
            dto.setCorreo(anonima.getCorreo());
            return dto;
        } else if (denuncia instanceof DenunciaPersonaReal) {
            DenunciaPersonaReal real = (DenunciaPersonaReal) denuncia;
            DenunciaPersonaRealResponseDTO dto = new DenunciaPersonaRealResponseDTO();
            dto.setId(real.getId());
            dto.setCodigoDenuncia(real.getCodigoDenuncia());
            dto.setEstado(real.getEstado());
            dto.setFechaIncidente(real.getFechaIncidente());
            dto.setHoraIncidente(real.getHoraIncidente());
            dto.setEsAhora(real.isEsAhora());
            dto.setDepartamento(real.getDepartamento());
            dto.setProvincia(real.getProvincia());
            dto.setDistrito(real.getDistrito());
            dto.setDescripcionHechos(real.getDescripcionHechos());
            dto.setRutasArchivos(real.getRutasArchivos());
            dto.setFechaRegistro(real.getFechaRegistro());
            dto.setAnonimo(real.isAnonimo());
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
        throw new IllegalArgumentException("Tipo de denuncia no reconocido para mapeo a DTO de respuesta en el controlador.");
    
    }

    // Endpoint para obtener una denuncia por ID (puede ser anónima o real)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or (hasRole('USUARIO_REGISTRADO') and @denunciaSecurity.isOwner(#id, authentication.principal.idUsuario))")
    public ResponseEntity<DenunciaResponseDTO> obtenerDenunciaPorId(@PathVariable Long id) {
        DenunciaResponseDTO denunciaDTO = denunciaService.obtenerDenunciaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia no encontrada con ID: " + id)); // <-- CAMBIO AQUÍ
        return ResponseEntity.ok(denunciaDTO);
    }

    // Endpoint para listar todas las denuncias (solo para ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<DenunciaResponseDTO>> listarTodasLasDenuncias() {
        List<DenunciaResponseDTO> dtoList = denunciaService.listarTodasLasDenuncias();
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint para listar denuncias anónimas (solo para ADMIN)
    @GetMapping("/anonimas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<DenunciaResponseDTO>> listarDenunciasAnonimas() {
        List<DenunciaResponseDTO> dtoList = denunciaService.listarDenunciasAnonimas();
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint para listar denuncias de persona real (solo para ADMIN)
    @GetMapping("/reales")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<DenunciaResponseDTO>> listarDenunciasPersonaReal() {
        List<DenunciaResponseDTO> dtoList = denunciaService.listarDenunciasPersonaReal();
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint para actualizar el estado de una denuncia (solo para ADMIN)
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<DenunciaResponseDTO> actualizarEstadoDenuncia(@PathVariable Long id, @RequestParam String nuevoEstado) {
        DenunciaResponseDTO denunciaActualizadaDTO = denunciaService.actualizarEstadoDenuncia(id, nuevoEstado);
        return ResponseEntity.ok(denunciaActualizadaDTO);
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
