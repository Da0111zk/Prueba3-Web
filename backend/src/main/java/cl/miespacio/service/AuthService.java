package cl.miespacio.service;

import cl.miespacio.dto.Dtos.AuthResponse;
import cl.miespacio.dto.Dtos.LoginRequest;
import cl.miespacio.dto.Dtos.RegistroRequest;
import cl.miespacio.model.Rol;
import cl.miespacio.model.Usuario;
import cl.miespacio.repository.UsuarioRepository;
import cl.miespacio.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder codificador;
    private final JwtService jwt;

    public AuthService(UsuarioRepository usuarios, PasswordEncoder codificador, JwtService jwt) {
        this.usuarios = usuarios;
        this.codificador = codificador;
        this.jwt = jwt;
    }

    public AuthResponse registrar(RegistroRequest r) {
        if (usuarios.existsByEmailIgnoreCase(r.email().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cuenta con ese correo.");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(r.nombre().trim());
        usuario.setEmail(r.email().trim().toLowerCase());
        usuario.setPasswordHash(codificador.encode(r.password()));
        usuario.setRol(Rol.CLIENTE);
        usuarios.save(usuario);
        return respuesta(usuario);
    }

    public AuthResponse login(LoginRequest r) {
        Usuario usuario = usuarios.findByEmailIgnoreCase(r.email().trim()).orElseThrow(AuthService::credencialesInvalidas);
        if (!codificador.matches(r.password(), usuario.getPasswordHash())) {
            throw credencialesInvalidas();
        }
        return respuesta(usuario);
    }

    private AuthResponse respuesta(Usuario usuario) {
        long expiraEn = System.currentTimeMillis() + jwt.getExpiracionMs();
        return new AuthResponse(jwt.generar(usuario), expiraEn, UsuarioService.aRespuesta(usuario));
    }

    private static ResponseStatusException credencialesInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
    }
}
