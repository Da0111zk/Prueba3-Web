package cl.miespacio.controller;

import cl.miespacio.dto.Dtos.UsuarioResponse;
import cl.miespacio.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Perfil de la sesión y administración de cuentas")
public class UsuarioController {
    private final UsuarioService usuarios;

    public UsuarioController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping("/me")
    @Operation(summary = "Devuelve el perfil del usuario autenticado (sirve para validar el token)")
    public UsuarioResponse perfil(Authentication auth) {
        return usuarios.perfil(auth.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista los usuarios (ADMIN)")
    public List<UsuarioResponse> listar() {
        return usuarios.listar();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Elimina un usuario y sus cotizaciones y reseñas (ADMIN)")
    public void eliminar(Authentication auth, @PathVariable Long id) {
        usuarios.eliminar(id, auth.getName());
    }
}
