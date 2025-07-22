
package com.themis.themis_backend.service;

import com.themis.themis_backend.dto.ContenidoDTO;
import com.themis.themis_backend.model.Contenido;
import com.themis.themis_backend.repository.ContenidoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

/**
 *
 * @author Nicole
 */
@Service
public class ContenidoService {
    private final ContenidoRepository contenidoRepository;

    public ContenidoService(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }
    
    @Transactional
    public ContenidoDTO createContenido(ContenidoDTO contenidoDTO){
        Contenido contenido = convertToEntity(contenidoDTO);
        Contenido savedContenido = contenidoRepository.save(contenido);
        return convertToDto(savedContenido);
    }
    
    @Transactional(readOnly = true)
    public List<ContenidoDTO> findAllPublicContents(){
        return contenidoRepository.findByEsPublicoTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<ContenidoDTO> findById(Long id){
        return contenidoRepository.findById(id)
                .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Optional<ContenidoDTO> findPublicContentById(Long id){
        return contenidoRepository.findById(id)
                .filter(Contenido::isEsPublico)
                .map(this::convertToDto);
    }
    
    @Transactional
    public ContenidoDTO updateContenido(Long id, ContenidoDTO contenidoDTO){
        return contenidoRepository.findById(id)
                .map(contenido -> {
                    contenido.setTitulo(contenidoDTO.getTitulo());
                    contenido.setContenido(contenidoDTO.getContenido());
                    contenido.setEsPublico(contenidoDTO.isEsPublico());
                    
                    return convertToDto(contenidoRepository.save(contenido));
                })
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado con ID: " + id));
    }
    
    @Transactional
    public void deleteContenido(Long id){
        if(!contenidoRepository.existsById(id)){
            throw new RuntimeException("Contenido no encontrado con ID: " + id);
        }
        contenidoRepository.deleteById(id);
    }
    
    private ContenidoDTO convertToDto(Contenido contenido){
        return new ContenidoDTO(
        contenido.getId(),
        contenido.getTitulo(),
        contenido.getContenido(),
        contenido.isEsPublico()
        );
    }
    
    private Contenido convertToEntity(ContenidoDTO contenidoDTO){
        Contenido contenido = new Contenido();
        
        contenido.setTitulo(contenidoDTO.getTitulo());
        contenido.setContenido(contenidoDTO.getContenido());
        contenido.setEsPublico(contenidoDTO.isEsPublico());
        
        return contenido;
    }
}
