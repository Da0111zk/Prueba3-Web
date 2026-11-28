package cl.miespacio.repository;

import cl.miespacio.model.Resena;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findAllByOrderByFechaDesc();

    boolean existsByUsuario_Id(Long usuarioId);

    void deleteByUsuario_Id(Long usuarioId);
}
