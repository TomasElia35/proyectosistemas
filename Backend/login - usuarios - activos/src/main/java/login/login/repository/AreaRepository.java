package login.login.repository;

import login.login.entidad.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findByNombre(String nombre);

    List<Area> findByEsActivaTrue();

    List<Area> findByIdResponsable(Long idResponsable);

    @Query("SELECT a FROM Area a WHERE a.centroCosto = :centroCosto AND a.esActiva = true")
    List<Area> findByCentroCosto(@Param("centroCosto") String centroCosto);

    @Query("SELECT a, COUNT(at) as cantidadActivos FROM Area a LEFT JOIN ActivoTecnologico at ON a.id = at.area.id WHERE a.esActiva = true GROUP BY a.id")
    List<Object[]> findAreasWithActivosCount();

    @Query("SELECT a, COUNT(e) as cantidadEmpleados FROM Area a LEFT JOIN Empleado e ON a.id = e.area.id WHERE a.esActiva = true GROUP BY a.id")
    List<Object[]> findAreasWithEmpleadosCount();
}