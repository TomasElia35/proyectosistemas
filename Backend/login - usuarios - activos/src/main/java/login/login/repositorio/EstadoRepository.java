package login.login.repositorio;

import login.login.entidad.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    // Buscar estados activos
    List<Estado> findByActivoTrue();

    // Buscar por nombre
    Optional<Estado> findByNombreIgnoreCase(String nombre);

    // Verificar si existe por nombre (excluyendo un ID específico para updates)
    boolean existsByNombreIgnoreCaseAndIdEstadoNot(String nombre, Integer idEstado);

    // Verificar si existe por nombre
    boolean existsByNombreIgnoreCase(String nombre);
}