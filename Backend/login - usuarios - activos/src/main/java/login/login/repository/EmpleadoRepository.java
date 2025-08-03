package login.login.repository;

import login.login.entidad.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByLegajo(String legajo);

    Optional<Empleado> findByDni(String dni);

    Optional<Empleado> findByEmailCorporativo(String emailCorporativo);

    List<Empleado> findByEsActivoTrue();

    List<Empleado> findByAreaIdAndEsActivoTrue(Long areaId);

    List<Empleado> findByEsResponsableAreaTrueAndEsActivoTrue();

    @Query("SELECT e FROM Empleado e WHERE e.tipoContrato = :tipoContrato AND e.esActivo = true")
    List<Empleado> findByTipoContrato(@Param("tipoContrato") Empleado.TipoContrato tipoContrato);

    @Query("SELECT e FROM Empleado e WHERE (e.nombre LIKE %:busqueda% OR e.apellido LIKE %:busqueda% OR e.legajo LIKE %:busqueda%) AND e.esActivo = true")
    List<Empleado> buscarEmpleados(@Param("busqueda") String busqueda);
}