package com.themis.themis_backend.service;

import com.themis.themis_backend.model.Rol;
import com.themis.themis_backend.model.Usuario;
import com.themis.themis_backend.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEnncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEnncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEnncoder = passwordEnncoder;
    }

    @Override
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        String contraseniaEncriptada = passwordEnncoder.encode(usuario.getContrasenia());
        usuario.setContrasenia(contraseniaEncriptada);
        if (usuario.getRoles().isEmpty()) {

        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en tu base de datos usando el UsuarioRepository
        // Aquí se busca por 'nombreUsuario', pero si tu login usa 'correoElectronico', cámbialo.
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Construye un objeto UserDetails (de Spring Security) a partir de tu entidad Usuario
        return new org.springframework.security.core.userdetails.User(
                usuario.getNombreUsuario(), // Nombre de usuario para Spring Security
                usuario.getContrasenia(), // Contraseña (Spring Security la comparará con la encriptada)
                usuario.isHabilitado(), // ¿Está habilitada la cuenta?
                true, // accountNonExpired: ¿La cuenta no ha expirado? (true por defecto para simplificar)
                true, // credentialsNonExpired: ¿Las credenciales no han expirado? (true por defecto)
                true, // accountNonLocked: ¿La cuenta no está bloqueada? (true por defecto)
                // Mapea tus roles (de tu enum Rol) a SimpleGrantedAuthority de Spring Security
                usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name())) // ¡Recuerda el prefijo "ROLE_"!
                        .collect(Collectors.toSet())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico) {
        return usuarioRepository.findByCorreoElectronico(correoElectronico);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuario.getIdUsuario()));

        usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
        usuarioExistente.setCorreoElectronico(usuario.getCorreoElectronico());

        usuarioExistente.setHabilitado(usuario.isHabilitado());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public Usuario cambiarHabilitado(Long idUsuario, boolean habilitado) {
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        usuarioExistente.setHabilitado(habilitado);
        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public Usuario asignarRolesAUsuario(Long idUsuario, Set<Rol> nuevosRoles) {
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        usuarioExistente.setRoles(nuevosRoles);
        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

}
