package cl.miespacio.service;

import cl.miespacio.dto.Dtos.UsuarioResponse;
import cl.miespacio.model.Usuario;
import cl.miespacio.repository.CotizacionRepository;
import cl.miespacio.repository.ResenaRepository;
import cl.miespacio.repository.UsuarioRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final CotizacionRepository cotizaciones;
    private final ResenaRepository resenas;

    public UsuarioService(UsuarioRepository usuarios, CotizacionRepository cotizaciones, ResenaRepository resenas) {
        this.usuarios = usuarios;
        this.cotizaciones = cotizaciones;
        this.resenas = resenas;
    }

    public List<UsuarioResponse> listar() {
        return usuarios.findAll(Sort.by("id")).stream().map(UsuarioService::aRespuesta).toList();
    }

    public UsuarioResponse perfil(String email) {
        return aRespuesta(buscarPorEmail(email));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarios.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La sesión ya no es válida."));
    }

    @Transactional
    public void eliminar(Long id, String emailSolicitante) {
        Usuario usuario = usuarios.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe."));
        if (usuario.getEmail().equalsIgnoreCase(emailSolicitante)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes eliminar tu propia cuenta.");
        }
        cotizaciones.deleteByUsuario_Id(id);
        resenas.deleteByUsuario_Id(id);
        usuarios.delete(usuario);
    }

    public static UsuarioResponse aRespuesta(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol());
    }
}
