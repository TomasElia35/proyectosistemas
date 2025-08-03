package login.login.repository;

import login.login.entidad.MovimientoActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoActivoRepository extends JpaRepository<MovimientoActivo, Long> {

    List<MovimientoActivo> findByActivoIdOrderByFechaMovimientoDesc(Long activoId);

    List<MovimientoActivo> findByEmpleadoOrigenIdOrderByFechaMovimientoDesc(Long empleadoId);

    List<MovimientoActivo> findByEmpleadoDestinoIdOrderByFechaMovimientoDesc(Long empleadoId);

    List<MovimientoActivo> findByAreaOrigenIdOrderByFechaMovimientoDesc(Long areaId);

    List<MovimientoActivo> findByAreaDestinoIdOrderByFechaMovimientoDesc(Long areaId);

    @Query("SELECT m FROM MovimientoActivo m WHERE m.tipoMovimiento = :tipo ORDER BY m.fechaMovimiento DESC")
    List<MovimientoActivo> findByTipoMovimiento(@Param("tipo") MovimientoActivo.TipoMovimiento tipo);

    @Query("SELECT m FROM MovimientoActivo m WHERE m.fechaMovimiento BETWEEN :fechaDesde AND :fechaHasta ORDER BY m.fechaMovimiento DESC")
    List<MovimientoActivo> findByFechaMovimientoBetween(@Param("fechaDesde") LocalDateTime fechaDesde, @Param("fechaHasta") LocalDateTime fechaHasta);

    @Query("SELECT m FROM MovimientoActivo m WHERE m.usuarioMovimiento = :usuario ORDER BY m.fechaMovimiento DESC")
    List<MovimientoActivo> findByUsuarioMovimiento(@Param("usuario") String usuario);
}