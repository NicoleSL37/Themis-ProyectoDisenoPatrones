package com.themis.themis_backend.controller;

import com.themis.themis_backend.dto.AuthenticationRequest;
import com.themis.themis_backend.dto.AuthenticationResponse;
import com.themis.themis_backend.model.Rol;
import com.themis.themis_backend.security.JwtService;
import com.themis.themis_backend.service.UsuarioServiceImpl;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioServiceImpl usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioServiceImpl usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        com.themis.themis_backend.model.Usuario authenticatedUserEntity = usuarioService.buscarPorNombreUsuario(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuario autenticado no encontrado en el sistema."));

        UserDetails userDetails = usuarioService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        Long userId = authenticatedUserEntity.getIdUsuario();
        Set<Rol> userRoles = authenticatedUserEntity.getRoles();

        return ResponseEntity.ok(new AuthenticationResponse(jwtToken, userDetails.getUsername(), userId, userRoles));
    }
}
