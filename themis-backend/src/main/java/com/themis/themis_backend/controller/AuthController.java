package com.themis.themis_backend.controller;

import com.themis.themis_backend.dto.AuthenticationRequest;
import com.themis.themis_backend.dto.AuthenticationResponse;
import com.themis.themis_backend.security.JwtService;
import com.themis.themis_backend.service.UsuarioServiceImpl;
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

        UserDetails userDetails = usuarioService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        Long userId = null;
        if (userDetails instanceof com.themis.themis_backend.model.Usuario) {
            userId = ((com.themis.themis_backend.model.Usuario) userDetails).getIdUsuario();
        } else {
            userId = usuarioService.buscarPorNombreUsuario(request.getUsername())
                    .map(u -> u.getIdUsuario())
                    .orElse(null);
        }
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken, userDetails.getUsername(), userId));
    }
}
