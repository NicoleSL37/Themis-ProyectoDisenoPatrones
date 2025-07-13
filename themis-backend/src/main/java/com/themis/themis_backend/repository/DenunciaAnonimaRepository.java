package com.themis.themis_backend.repository;

import com.themis.themis_backend.model.DenunciaAnonima;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaAnonimaRepository extends JpaRepository<DenunciaAnonima, Long> {

    Optional<DenunciaAnonima> findByCodigoDenuncia(String codigoDenuncia);

}
