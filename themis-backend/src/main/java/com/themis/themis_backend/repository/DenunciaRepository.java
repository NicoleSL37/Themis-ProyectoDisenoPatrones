package com.themis.themis_backend.repository;

import com.themis.themis_backend.model.Denuncia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    List<Denuncia> findByAnonimo(boolean anonimo);
}
