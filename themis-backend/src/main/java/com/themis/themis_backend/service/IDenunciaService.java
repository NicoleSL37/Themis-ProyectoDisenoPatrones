package com.themis.themis_backend.service;

import com.themis.themis_backend.model.Denuncia;
import com.themis.themis_backend.model.DenunciaAnonima;
import com.themis.themis_backend.model.DenunciaPersonaReal;
import java.util.List;
import java.util.Optional;

public interface IDenunciaService {
    DenunciaAnonima crearDenunciaAnonima(DenunciaAnonima denunciaAnonima);
    DenunciaPersonaReal crearDenunciaPersonaReal(DenunciaPersonaReal denunciaPersonaReal);
    Optional<Denuncia> obtenerDenunciaPorId(Long id);
    List<Denuncia> listarTodasLasDenuncias();
    List<Denuncia> listarDenunciasAnonimas();
    List<Denuncia> listarDenunciasPersonaReal();
    Denuncia actualizarEstadoDenuncia(Long id, String nuevoEstado); 
    void eliminarDenuncia(Long id);
}
