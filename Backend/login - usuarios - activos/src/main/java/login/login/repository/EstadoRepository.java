package login.login.repository;

import login.login.entidad.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    static Optional<Estado> findByNombre(String nombre);

    List<Estado> findByEsActivoTrue();

    List<Estado> findByPermiteAsignacionTrueAndEsActivoTrue();

    List<Estado> findByRequiereAprobacionTrueAndEsActivoTrue();

    List<Estado> findByEsEstadoFinalTrueAndEsActivoTrue();
}