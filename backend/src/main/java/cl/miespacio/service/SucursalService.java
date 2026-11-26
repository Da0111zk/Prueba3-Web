package cl.miespacio.service;

import cl.miespacio.dto.Dtos.SucursalRequest;
import cl.miespacio.dto.Dtos.SucursalResponse;
import cl.miespacio.model.Sucursal;
import cl.miespacio.repository.SucursalRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SucursalService {
    private final SucursalRepository repositorio;

    public SucursalService(SucursalRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<SucursalResponse> listar() {
        return repositorio.findAll(Sort.by("id")).stream().map(SucursalService::aRespuesta).toList();
    }

    public SucursalResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public SucursalResponse crear(SucursalRequest r) {
        Sucursal sucursal = new Sucursal();
        copiar(sucursal, r);
        return aRespuesta(repositorio.save(sucursal));
    }

    @Transactional
    public SucursalResponse actualizar(Long id, SucursalRequest r) {
        Sucursal sucursal = buscar(id);
        copiar(sucursal, r);
        return aRespuesta(repositorio.save(sucursal));
    }

    @Transactional
    public void eliminar(Long id) {
        repositorio.delete(buscar(id));
    }

    private Sucursal buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe."));
    }

    private static void copiar(Sucursal sucursal, SucursalRequest r) {
        sucursal.setNombre(r.nombre().trim());
        sucursal.setDireccion(r.direccion().trim());
        sucursal.setHorario(r.horario().trim());
        sucursal.setAcceso(r.acceso().trim());
        sucursal.setTelefono(r.telefono().trim());
        sucursal.setMapaUrl(r.mapaUrl() == null || r.mapaUrl().isBlank() ? null : r.mapaUrl().trim());
    }

    private static SucursalResponse aRespuesta(Sucursal s) {
        return new SucursalResponse(s.getId(), s.getNombre(), s.getDireccion(), s.getHorario(), s.getAcceso(),
                s.getTelefono(), s.getMapaUrl());
    }
}