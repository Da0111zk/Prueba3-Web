package cl.miespacio.controller;

import cl.miespacio.dto.Dtos.SucursalRequest;
import cl.miespacio.dto.Dtos.SucursalResponse;
import cl.miespacio.service.SucursalService;
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
@RequestMapping("/api/sucursales")
@Tag(name = "Sucursales", description = "Ubicaciones de Mi Espacio (lectura pública, escritura solo ADMIN)")
public class SucursalController {
    private final SucursalService sucursales;

    public SucursalController(SucursalService sucursales) {
        this.sucursales = sucursales;
    }

    @GetMapping
    @Operation(summary = "Lista todas las sucursales")
    public List<SucursalResponse> listar() {
        return sucursales.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una sucursal por su id")
    public SucursalResponse obtener(@PathVariable Long id) {
        return sucursales.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea una sucursal (ADMIN)")
    public SucursalResponse crear(@Valid @RequestBody SucursalRequest r) {
        return sucursales.crear(r);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualiza una sucursal (ADMIN)")
    public SucursalResponse actualizar(@PathVariable Long id, @Valid @RequestBody SucursalRequest r) {
        return sucursales.actualizar(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Elimina una sucursal (ADMIN)")
    public void eliminar(@PathVariable Long id) {
        sucursales.eliminar(id);
    }
}
