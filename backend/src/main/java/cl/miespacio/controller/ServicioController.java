package cl.miespacio.controller;

import cl.miespacio.dto.Dtos.ServicioRequest;
import cl.miespacio.dto.Dtos.ServicioResponse;
import cl.miespacio.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/servicios")
@Tag(name = "Servicios", description = "Catálogo de lockers y bodegas (lectura pública, escritura solo ADMIN)")
public class ServicioController {
    private final ServicioService servicios;

    public ServicioController(ServicioService servicios) {
        this.servicios = servicios;
    }

    @GetMapping
    @Operation(summary = "Lista todos los servicios")
    public List<ServicioResponse> listar() {
        return servicios.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un servicio por su id")
    public ServicioResponse obtener(@PathVariable Long id) {
        return servicios.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea un servicio (ADMIN)")
    public ServicioResponse crear(@Valid @RequestBody ServicioRequest r) {
        return servicios.crear(r);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualiza un servicio (ADMIN)")
    public ServicioResponse actualizar(@PathVariable Long id, @Valid @RequestBody ServicioRequest r) {
        return servicios.actualizar(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Elimina un servicio sin cotizaciones asociadas (ADMIN)")
    public void eliminar(@PathVariable Long id) {
        servicios.eliminar(id);
    }
}
