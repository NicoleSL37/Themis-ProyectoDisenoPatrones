package com.themis.themis_backend.service;

import com.themis.themis_backend.dto.DenunciaAnonimaResponseDTO;
import com.themis.themis_backend.dto.DenunciaPersonaRealResponseDTO;
import com.themis.themis_backend.dto.DenunciaResponseDTO;
import com.themis.themis_backend.exception.ResourceNotFoundException;
import com.themis.themis_backend.model.Denuncia;
import com.themis.themis_backend.model.DenunciaAnonima;
import com.themis.themis_backend.model.DenunciaPersonaReal;
import com.themis.themis_backend.repository.DenunciaAnonimaRepository;
import com.themis.themis_backend.repository.DenunciaPersonaRealRepository;
import com.themis.themis_backend.repository.DenunciaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaServiceImpl implements IDenunciaService {
    private final DenunciaAnonimaRepository denunciaAnonimaRepository;
    private final DenunciaPersonaRealRepository denunciaPersonaRealRepository;
    private final DenunciaRepository denunciaRepository;
    
    public DenunciaServiceImpl(DenunciaAnonimaRepository denunciaAnonimaRepository,
                               DenunciaPersonaRealRepository denunciaPersonaRealRepository,
                               DenunciaRepository denunciaRepository) { // Agregado DenunciaRepository
        this.denunciaAnonimaRepository = denunciaAnonimaRepository;
        this.denunciaPersonaRealRepository = denunciaPersonaRealRepository;
        this.denunciaRepository = denunciaRepository;
    }

    @Override
    @Transactional
    public DenunciaAnonima crearDenunciaAnonima(DenunciaAnonima denunciaAnonima) {
        return denunciaRepository.save(denunciaAnonima);
    }

    @Override
    @Transactional
    public DenunciaPersonaReal crearDenunciaPersonaReal(DenunciaPersonaReal denunciaPersonaReal) {
        return denunciaRepository.save(denunciaPersonaReal);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DenunciaResponseDTO> obtenerDenunciaPorId(Long id) {
        return denunciaRepository.findById(id)
                .map(this::convertToDenunciaResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DenunciaResponseDTO> listarTodasLasDenuncias() {
        return denunciaRepository.findAll().stream()
                .map(this::convertToDenunciaResponseDTO)
                .collect(Collectors.toList());
    }            
        
    @Override
    @Transactional(readOnly = true)
    public List<DenunciaResponseDTO> listarDenunciasAnonimas() {
        return denunciaAnonimaRepository.findAll().stream()
                .map(this::convertToDenunciaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DenunciaResponseDTO> listarDenunciasPersonaReal() {
        return denunciaPersonaRealRepository.findAll().stream()
                .map(this::convertToDenunciaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DenunciaResponseDTO actualizarEstadoDenuncia(Long id, String nuevoEstado) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia no encontrada con ID: " + id));
        denuncia.setEstado(nuevoEstado);
        Denuncia updatedDenuncia = denunciaRepository.save(denuncia);
        return convertToDenunciaResponseDTO(updatedDenuncia);
    }
    
    @Override
    @Transactional
    public void eliminarDenuncia(Long id) {
        if (!denunciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Denuncia no encontrada con ID: " + id);
        }
        denunciaRepository.deleteById(id);
    }
    
    private DenunciaResponseDTO convertToDenunciaResponseDTO(Denuncia denuncia){
        if (denuncia instanceof DenunciaAnonima) {
            DenunciaAnonima anonima = (DenunciaAnonima) denuncia;
            DenunciaAnonimaResponseDTO dto = new DenunciaAnonimaResponseDTO();
            mapCommonFields(denuncia, dto); // Mapea campos comunes
            dto.setCorreo(anonima.getCorreo()); // Mapea campo específico
            return dto;
        } else if (denuncia instanceof DenunciaPersonaReal) {
            DenunciaPersonaReal real = (DenunciaPersonaReal) denuncia;
            DenunciaPersonaRealResponseDTO dto = new DenunciaPersonaRealResponseDTO();
            mapCommonFields(denuncia, dto); // Mapea campos comunes
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
        throw new IllegalArgumentException("Tipo de denuncia no reconocido para mapeo a DTO de respuesta.");
    }
    
    
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
    
    
    
}
