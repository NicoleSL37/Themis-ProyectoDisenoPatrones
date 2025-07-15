package com.themis.themis_backend.security;

import com.themis.themis_backend.model.DenunciaPersonaReal;
import com.themis.themis_backend.repository.DenunciaRepository;
import org.springframework.stereotype.Component;

@Component("denunciaSecurity")
public class DenunciaSecurity {

    private final DenunciaRepository denunciaRepository;

    public DenunciaSecurity(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    public boolean isOwner(Long denunciaId, Long userId) {
        return denunciaRepository.findById(denunciaId).map(denuncia -> {
            if (denuncia instanceof DenunciaPersonaReal) {
                DenunciaPersonaReal real = (DenunciaPersonaReal) denuncia;

                return real.getUsuario() != null && real.getUsuario().getIdUsuario().equals(userId);
            }
            return false; 
        }).orElse(false);
    }
}
