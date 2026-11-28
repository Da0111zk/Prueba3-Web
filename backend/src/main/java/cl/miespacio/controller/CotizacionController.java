package cl.miespacio.controller;

import cl.miespacio.dto.Dtos.CotizacionRequest;
import cl.miespacio.dto.Dtos.CotizacionResponse;
import cl.miespacio.dto.Dtos.CotizacionUpdateRequest;
import cl.miespacio.security.Sesion;
import cl.miespacio.service.CotizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/cotizaciones")
@Tag(name = "Cotizaciones", description = "Solicitudes de cotización (requieren sesión)")
public class CotizacionController {
    private final CotizacionService cotizaciones;

    public CotizacionController(CotizacionService cotizaciones) {
        this.cotizaciones = cotizaciones;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todas las cotizaciones (ADMIN)")
    public List<CotizacionResponse> listarTodas() {
        return cotizaciones.listarTodas();
    }

    @GetMapping("/mis")
    @Operation(summary = "Lista las cotizaciones del usuario autenticado")
    public List<CotizacionResponse> listarPropias(Authentication auth) {
        return cotizaciones.listarPropias(auth.getName());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una cotización (su dueño o un ADMIN)")
    public CotizacionResponse obtener(Authentication auth, @PathVariable Long id) {
        return cotizaciones.obtener(id, auth.getName(), Sesion.esAdmin(auth));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Solicita una cotización (una pendiente por servicio y usuario)")
    public CotizacionResponse crear(Authentication auth, @Valid @RequestBody CotizacionRequest r) {
        return cotizaciones.crear(auth.getName(), r);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Dueño: edita el mensaje. ADMIN: cambia el estado a ATENDIDA o RECHAZADA. Solo si está PENDIENTE")
    public CotizacionResponse actualizar(Authentication auth, @PathVariable Long id,
                                         @Valid @RequestBody CotizacionUpdateRequest r) {
        return cotizaciones.actualizar(id, auth.getName(), Sesion.esAdmin(auth), r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Elimina una cotización (dueño si está PENDIENTE, o ADMIN)")
    public void eliminar(Authentication auth, @PathVariable Long id) {
        cotizaciones.eliminar(id, auth.getName(), Sesion.esAdmin(auth));
    }
}
