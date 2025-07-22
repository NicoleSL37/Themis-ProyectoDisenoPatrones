
package com.themis.themis_backend.repository;

import com.themis.themis_backend.model.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author Nicole
 */
public interface ContenidoRepository extends JpaRepository<Contenido, Long>{
    List<Contenido> findByEsPublicoTrue();
}
