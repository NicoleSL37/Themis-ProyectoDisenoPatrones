package com.themis.themis_backend;

import com.themis.themis_backend.model.Usuario;
import com.themis.themis_backend.repository.UsuarioRepository;
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
            }
        };
    }

}
