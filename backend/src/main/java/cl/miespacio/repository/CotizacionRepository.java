package cl.miespacio.repository;

import cl.miespacio.model.Cotizacion;
import cl.miespacio.model.EstadoCotizacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    List<Cotizacion> findAllByOrderByCreadaEnDesc();

    List<Cotizacion> findByUsuario_IdOrderByCreadaEnDesc(Long usuarioId);

    boolean existsByUsuario_IdAndServicio_IdAndEstado(Long usuarioId, Long servicioId, EstadoCotizacion estado);

    boolean existsByServicio_Id(Long servicioId);

    void deleteByUsuario_Id(Long usuarioId);
}
