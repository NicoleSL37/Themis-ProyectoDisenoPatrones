package com.themis.themis_backend;

import com.themis.themis_backend.model.Rol;
import com.themis.themis_backend.model.Usuario;
import com.themis.themis_backend.repository.UsuarioRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ThemisBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThemisBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner createInitialUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            if (usuarioRepository.findByCorreoElectronico("admin@gmail.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("admin");
                admin.setCorreoElectronico("admin@gmail.com");
                admin.setContrasenia(passwordEncoder.encode("adminpass"));
                admin.setHabilitado(true);
                
                Set<Rol> roles = new HashSet<>();
                roles.add(Rol.ADMINISTRADOR);
                admin.setRoles(roles);
                
                usuarioRepository.save(admin);
                System.out.println("Usuario administrador inicial 'admin@gmail.com' creado.");
            }
        };
    }

}
