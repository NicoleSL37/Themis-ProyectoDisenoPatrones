package com.themis.themis_backend.config;

import com.themis.themis_backend.security.JwtAuthenticationFilter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;


    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, @Lazy UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // <<-- Usa tu UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // <<-- Usa tu PasswordEncoder
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para APIs REST
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                // Permite el acceso sin autenticación a las rutas de registro y autenticación
                .requestMatchers(
                        "/api/usuarios/registro",
                        "/api/auth/**",
                        "/api/denuncias-anonimas",      // <--- Asegúrate de que estén aquí
                        
                        "/api/denuncias-anonimas/archivo/**",
                        "/api/denuncias-personas-reales",// <--- Asegúrate de que estén aquí
                        "/api/admin/**",
                        "/api/consultar-denuncia",
                        "/api/denuncias/descargar-archivos/**",
                        "/error").permitAll() // <<-- Añade /api/auth/**
                // Permite el acceso a la consola H2 (solo para desarrollo)

                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // <<-- 3. Configura la política de sesión sin estado
                )
                .authenticationProvider(authenticationProvider()) // <<-- 4. Registra tu AuthenticationProvider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // <<-- 5. Añade tu filtro JWT antes del filtro de autenticación de usuario/contraseña

        // Si usas H2 Console, necesitas deshabilitar la protección de frames
        // http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        return http.build();
    }
    
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return (web) -> web.ignoring().requestMatchers("/uploads/**", "/api/denuncias/descargar-archivos/**");
//    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // AÑADE TODOS LOS ORIGENES DESDE LOS QUE TU FRONTEND VA A OPERAR
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5502", "http://127.0.0.1:5502", "http://127.0.0.7:5502"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Permite todos los encabezados
        configuration.setAllowCredentials(true); // Permite credenciales (cookies, auth headers)
        configuration.setMaxAge(3600L); // Cache de la preflight request por 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // Aplica esta configuración a TODAS las rutas
        return source;
    }
}
