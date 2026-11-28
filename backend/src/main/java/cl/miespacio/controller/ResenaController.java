package cl.miespacio.controller;

import cl.miespacio.dto.Dtos.ResenaRequest;
import cl.miespacio.dto.Dtos.ResenaResponse;
import cl.miespacio.security.Sesion;
import cl.miespacio.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resenas")
@Tag(name = "Reseñas", description = "Opiniones de clientes (lectura pública; escribir requiere sesión)")
public class ResenaController {
    private final ResenaService resenas;

    public ResenaController(ResenaService resenas) {
        this.resenas = resenas;
    }

    @GetMapping
    @Operation(summary = "Lista las reseñas, de la más reciente a la más antigua")
    public List<ResenaResponse> listar() {
        return resenas.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una reseña por su id")
    public ResenaResponse obtener(@PathVariable Long id) {
        return resenas.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Publica la reseña del usuario autenticado (máximo una por usuario)")
    public ResenaResponse crear(Authentication auth, @Valid @RequestBody ResenaRequest r) {
        return resenas.crear(auth.getName(), r);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita una reseña (su autor o un ADMIN)")
    public ResenaResponse actualizar(Authentication auth, @PathVariable Long id, @Valid @RequestBody ResenaRequest r) {
        return resenas.actualizar(id, auth.getName(), Sesion.esAdmin(auth), r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Elimina una reseña (su autor o un ADMIN)")
    public void eliminar(Authentication auth, @PathVariable Long id) {
        resenas.eliminar(id, auth.getName(), Sesion.esAdmin(auth));
    }
}
