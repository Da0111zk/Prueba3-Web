package cl.miespacio.service;

import cl.miespacio.dto.Dtos.CotizacionRequest;
import cl.miespacio.dto.Dtos.CotizacionResponse;
import cl.miespacio.dto.Dtos.CotizacionUpdateRequest;
import cl.miespacio.model.Cotizacion;
import cl.miespacio.model.EstadoCotizacion;
import cl.miespacio.model.Servicio;
import cl.miespacio.model.Usuario;
import cl.miespacio.repository.CotizacionRepository;
import cl.miespacio.repository.ServicioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CotizacionService {
    private final CotizacionRepository repositorio;
    private final ServicioRepository servicios;
    private final UsuarioService usuarios;

    public CotizacionService(CotizacionRepository repositorio, ServicioRepository servicios, UsuarioService usuarios) {
        this.repositorio = repositorio;
        this.servicios = servicios;
        this.usuarios = usuarios;
    }

    @Transactional
    public CotizacionResponse crear(String email, CotizacionRequest r) {
        Usuario usuario = usuarios.buscarPorEmail(email);
        Servicio servicio = servicios.findById(r.servicioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio solicitado no existe."));
        if (repositorio.existsByUsuario_IdAndServicio_IdAndEstado(usuario.getId(), servicio.getId(), EstadoCotizacion.PENDIENTE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya tienes una cotización pendiente para este servicio.");
        }
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setUsuario(usuario);
        cotizacion.setServicio(servicio);
        cotizacion.setMensaje(r.mensaje().trim());
        cotizacion.setEstado(EstadoCotizacion.PENDIENTE);
        return aRespuesta(repositorio.save(cotizacion));
    }

    public List<CotizacionResponse> listarTodas() {
        return repositorio.findAllByOrderByCreadaEnDesc().stream().map(CotizacionService::aRespuesta).toList();
    }

    public List<CotizacionResponse> listarPropias(String email) {
        Usuario usuario = usuarios.buscarPorEmail(email);
        return repositorio.findByUsuario_IdOrderByCreadaEnDesc(usuario.getId()).stream()
                .map(CotizacionService::aRespuesta).toList();
    }

    public CotizacionResponse obtener(Long id, String email, boolean esAdmin) {
        Cotizacion cotizacion = buscar(id);
        verificarAcceso(cotizacion, email, esAdmin);
        return aRespuesta(cotizacion);
    }

    @Transactional
    public CotizacionResponse actualizar(Long id, String email, boolean esAdmin, CotizacionUpdateRequest r) {
        Cotizacion cotizacion = buscar(id);
        verificarAcceso(cotizacion, email, esAdmin);
        if (cotizacion.getEstado() != EstadoCotizacion.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se pueden modificar cotizaciones pendientes.");
        }
        if (esAdmin) {
            if (r.estado() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el nuevo estado.");
            }
            if (r.estado() == EstadoCotizacion.PENDIENTE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado debe ser ATENDIDA o RECHAZADA.");
            }
            cotizacion.setEstado(r.estado());
        } else {
            if (r.estado() != null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo un administrador puede cambiar el estado.");
            }
            if (r.mensaje() == null || r.mensaje().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el nuevo mensaje.");
            }
            cotizacion.setMensaje(r.mensaje().trim());
        }
        return aRespuesta(repositorio.save(cotizacion));
    }

    @Transactional
    public void eliminar(Long id, String email, boolean esAdmin) {
        Cotizacion cotizacion = buscar(id);
        verificarAcceso(cotizacion, email, esAdmin);
        if (!esAdmin && cotizacion.getEstado() != EstadoCotizacion.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo puedes eliminar cotizaciones pendientes.");
        }
        repositorio.delete(cotizacion);
    }

    private Cotizacion buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cotización no existe."));
    }

    private static void verificarAcceso(Cotizacion cotizacion, String email, boolean esAdmin) {
        if (!esAdmin && !cotizacion.getUsuario().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta cotización.");
        }
    }

    private static CotizacionResponse aRespuesta(Cotizacion c) {
        return new CotizacionResponse(c.getId(), c.getServicio().getId(), c.getServicio().getNombre(),
                c.getUsuario().getNombre(), c.getUsuario().getEmail(), c.getMensaje(), c.getEstado(), c.getCreadaEn());
    }
}
