package com.themis.themis_backend.repository;

import com.themis.themis_backend.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByCorreoElectronico(String correoElectronico);

}
