package com.themis.themis_backend.service;

import com.themis.themis_backend.exception.ResourceNotFoundException;
import com.themis.themis_backend.model.Denuncia;
import com.themis.themis_backend.model.DenunciaAnonima;
import com.themis.themis_backend.model.DenunciaPersonaReal;
import com.themis.themis_backend.repository.DenunciaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaServiceImpl implements IDenunciaService {

    private final DenunciaRepository denunciaRepository;

    public DenunciaServiceImpl(DenunciaRepository denunciaRepository) {
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
    public Optional<Denuncia> obtenerDenunciaPorId(Long id) {
        return denunciaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> listarTodasLasDenuncias() {
        return denunciaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> listarDenunciasAnonimas() {

        return denunciaRepository.findByAnonimo(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> listarDenunciasPersonaReal() {

        return denunciaRepository.findByAnonimo(false);
    }

    @Override
    @Transactional
    public Denuncia actualizarEstadoDenuncia(Long id, String nuevoEstado) {
        Denuncia denuncia = denunciaRepository.findById(id)
                // <-- CAMBIO AQUÍ: Reemplazar RuntimeException por ResourceNotFoundException
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia no encontrada con ID: " + id));
        denuncia.setEstado(nuevoEstado);

        return denunciaRepository.save(denuncia);
    }

    @Override
    @Transactional
    public void eliminarDenuncia(Long id) {
        if (!denunciaRepository.existsById(id)) {
            // <-- CAMBIO AQUÍ: Reemplazar RuntimeException por ResourceNotFoundException
            throw new ResourceNotFoundException("Denuncia no encontrada con ID: " + id);
        }
        denunciaRepository.deleteById(id);
    }
}
