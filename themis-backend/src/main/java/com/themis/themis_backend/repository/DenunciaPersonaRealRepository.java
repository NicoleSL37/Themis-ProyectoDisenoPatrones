package com.themis.themis_backend.repository;

import com.themis.themis_backend.model.DenunciaPersonaReal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaPersonaRealRepository extends JpaRepository<DenunciaPersonaReal, Long> {

    Optional<DenunciaPersonaReal> findByCodigoDenunciaAndTipoDocumentoAndNumeroDocumento(String codigoDenuncia, String tipoDocumento, String numeroDocumento);

    Optional<DenunciaPersonaReal> findByCodigoDenuncia(String codigoDenuncia);

    Optional<DenunciaPersonaReal> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);

    Optional<DenunciaPersonaReal> findByTipoDocumentoAndNumeroDocumentoAndCodigoDenuncia(String tipoDocumento, String numeroDocumento, String codigoDenuncia);
}
