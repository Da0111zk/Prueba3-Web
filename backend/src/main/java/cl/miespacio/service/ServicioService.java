package cl.miespacio.service;

import cl.miespacio.dto.Dtos.ServicioRequest;
import cl.miespacio.dto.Dtos.ServicioResponse;
import cl.miespacio.model.Servicio;
import cl.miespacio.repository.CotizacionRepository;
import cl.miespacio.repository.ServicioRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServicioService {
    private final ServicioRepository repositorio;
    private final CotizacionRepository cotizaciones;

    public ServicioService(ServicioRepository repositorio, CotizacionRepository cotizaciones) {
        this.repositorio = repositorio;
        this.cotizaciones = cotizaciones;
    }

    public List<ServicioResponse> listar() {
        return repositorio.findAll(Sort.by("id")).stream().map(ServicioService::aRespuesta).toList();
    }

    public ServicioResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public ServicioResponse crear(ServicioRequest r) {
        Servicio servicio = new Servicio();
        copiar(servicio, r);
        return aRespuesta(repositorio.save(servicio));
    }

    @Transactional
    public ServicioResponse actualizar(Long id, ServicioRequest r) {
        Servicio servicio = buscar(id);
        copiar(servicio, r);
        return aRespuesta(repositorio.save(servicio));
    }

    @Transactional
    public void eliminar(Long id) {
        Servicio servicio = buscar(id);
        if (cotizaciones.existsByServicio_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar un servicio con cotizaciones asociadas.");
        }
        repositorio.delete(servicio);
    }

    private Servicio buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio no existe."));
    }

    private static void copiar(Servicio servicio, ServicioRequest r) {
        servicio.setNombre(r.nombre().trim());
        servicio.setDescripcion(r.descripcion().trim());
        servicio.setCategoria(r.categoria().trim());
        servicio.setTamano(r.tamano().trim());
        servicio.setImagen(r.imagen() == null || r.imagen().isBlank() ? null : r.imagen().trim());
    }

    private static ServicioResponse aRespuesta(Servicio s) {
        return new ServicioResponse(s.getId(), s.getNombre(), s.getDescripcion(), s.getCategoria(), s.getTamano(), s.getImagen());
    }
}
