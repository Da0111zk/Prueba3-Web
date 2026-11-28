package cl.miespacio.service;

import cl.miespacio.dto.Dtos.ResenaRequest;
import cl.miespacio.dto.Dtos.ResenaResponse;
import cl.miespacio.model.Resena;
import cl.miespacio.model.Usuario;
import cl.miespacio.repository.ResenaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResenaService {
    private final ResenaRepository repositorio;
    private final UsuarioService usuarios;

    public ResenaService(ResenaRepository repositorio, UsuarioService usuarios) {
        this.repositorio = repositorio;
        this.usuarios = usuarios;
    }

    public List<ResenaResponse> listar() {
        return repositorio.findAllByOrderByFechaDesc().stream().map(ResenaService::aRespuesta).toList();
    }

    public ResenaResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public ResenaResponse crear(String email, ResenaRequest r) {
        Usuario usuario = usuarios.buscarPorEmail(email);
        if (repositorio.existsByUsuario_Id(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya publicaste una reseña. Puedes editarla o eliminarla.");
        }
        Resena resena = new Resena();
        resena.setUsuario(usuario);
        resena.setAutor(usuario.getNombre());
        resena.setTexto(r.texto().trim());
        resena.setEvaluacion(r.evaluacion());
        resena.setFecha(LocalDateTime.now());
        return aRespuesta(repositorio.save(resena));
    }

    @Transactional
    public ResenaResponse actualizar(Long id, String email, boolean esAdmin, ResenaRequest r) {
        Resena resena = buscar(id);
        verificarPermiso(resena, email, esAdmin);
        resena.setTexto(r.texto().trim());
        resena.setEvaluacion(r.evaluacion());
        return aRespuesta(repositorio.save(resena));
    }

    @Transactional
    public void eliminar(Long id, String email, boolean esAdmin) {
        Resena resena = buscar(id);
        verificarPermiso(resena, email, esAdmin);
        repositorio.delete(resena);
    }

    private Resena buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La reseña no existe."));
    }

    private static void verificarPermiso(Resena resena, String email, boolean esAdmin) {
        boolean esAutor = resena.getUsuario() != null && resena.getUsuario().getEmail().equalsIgnoreCase(email);
        if (!esAdmin && !esAutor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo el autor o un administrador puede modificar esta reseña.");
        }
    }

    private static ResenaResponse aRespuesta(Resena r) {
        Long usuarioId = r.getUsuario() == null ? null : r.getUsuario().getId();
        return new ResenaResponse(r.getId(), r.getAutor(), r.getTexto(), r.getEvaluacion(), r.getFecha(), usuarioId);
    }
}
