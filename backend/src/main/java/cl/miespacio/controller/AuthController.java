package cl.miespacio.controller;

import cl.miespacio.dto.Dtos.AuthResponse;
import cl.miespacio.dto.Dtos.LoginRequest;
import cl.miespacio.dto.Dtos.RegistroRequest;
import cl.miespacio.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión (devuelve un token JWT)")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra un cliente nuevo y devuelve su token JWT")
    public AuthResponse registro(@Valid @RequestBody RegistroRequest r) {
        return auth.registrar(r);
    }

    @PostMapping("/login")
    @Operation(summary = "Inicia sesión con correo y contraseña y devuelve un token JWT")
    public AuthResponse login(@Valid @RequestBody LoginRequest r) {
        return auth.login(r);
    }
}
