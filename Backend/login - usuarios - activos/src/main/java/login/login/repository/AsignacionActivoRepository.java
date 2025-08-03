package login.login.repository;

import login.login.entidad.AsignacionActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionActivoRepository extends JpaRepository<AsignacionActivo, Long> {

    @Query("SELECT aa FROM AsignacionActivo aa WHERE aa.activo.id = :activoId AND aa.fechaDesasignacion IS NULL")
    static Optional<AsignacionActivo> findAsignacionActivaByActivoId(@Param("activoId") Long activoId);

    @Query("SELECT aa FROM AsignacionActivo aa WHERE aa.empleado.id = :empleadoId AND aa.fechaDesasignacion IS NULL")
    static List<AsignacionActivo> findAsignacionesActivasByEmpleadoId(@Param("empleadoId") Long empleadoId);

    @Query("SELECT aa FROM AsignacionActivo aa WHERE aa.area.id = :areaId AND aa.fechaDesasignacion IS NULL")
    List<AsignacionActivo> findAsignacionesActivasByAreaId(@Param("areaId") Long areaId);

    List<AsignacionActivo> findByActivoIdOrderByFechaAsignacionDesc(Long activoId);

    List<AsignacionActivo> findByEmpleadoIdOrderByFechaAsignacionDesc(Long empleadoId);

    @Query("SELECT aa FROM AsignacionActivo aa WHERE aa.fechaAsignacion BETWEEN :fechaDesde AND :fechaHasta")
    List<AsignacionActivo> findByFechaAsignacionBetween(@Param("fechaDesde") LocalDateTime fechaDesde, @Param("fechaHasta") LocalDateTime fechaHasta);

    @Query("SELECT COUNT(aa) FROM AsignacionActivo aa WHERE aa.empleado.id = :empleadoId AND aa.fechaDesasignacion IS NULL")
    Long countAsignacionesActivasByEmpleadoId(@Param("empleadoId") Long empleadoId);
}