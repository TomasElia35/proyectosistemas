package login.login.repositorio;

import login.login.modelo.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ActivoTecnologicoRepository extends JpaRepository<ActivoTecnologico, Long> {

    Optional<ActivoTecnologico> findByCodigoInventario(String codigoInventario);

    Optional<ActivoTecnologico> findByCodigoTecnico(String codigoTecnico);

    List<ActivoTecnologico> findByEmpleadoId(Long idEmpleado);

    List<ActivoTecnologico> findByActivoTrue();
}
