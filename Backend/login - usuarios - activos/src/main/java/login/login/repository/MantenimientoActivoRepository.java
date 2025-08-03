package login.login.repository;

import login.login.entidad.MantenimientoActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MantenimientoActivoRepository extends JpaRepository<MantenimientoActivo, Long> {

    List<MantenimientoActivo> findByActivoIdOrderByFechaProgramadaDesc(Long activoId);

    List<MantenimientoActivo> findByProveedorIdOrderByFechaProgramadaDesc(Long proveedorId);

    @Query("SELECT m FROM MantenimientoActivo m WHERE m.fechaRealizada IS NULL")
    List<MantenimientoActivo> findMantenimientosPendientes();

    @Query("SELECT m FROM MantenimientoActivo m WHERE m.fechaRealizada IS NULL AND m.fechaProgramada < CURRENT_DATE")
    List<MantenimientoActivo> findMantenimientosAtrasados();

    @Query("SELECT m FROM MantenimientoActivo m WHERE m.fechaRealizada IS NOT NULL")
    List<MantenimientoActivo> findMantenimientosCompletados();

    @Query("SELECT m FROM MantenimientoActivo m WHERE m.tipoMantenimiento = :tipo")
    List<MantenimientoActivo> findByTipoMantenimiento(@Param("tipo") MantenimientoActivo.TipoMantenimiento tipo);

    @Query("SELECT m FROM MantenimientoActivo m WHERE m.fechaProgramada BETWEEN :fechaDesde AND :fechaHasta")
    List<MantenimientoActivo> findByFechaProgramadaBetween(@Param("fechaDesde") LocalDate fechaDesde, @Param("fechaHasta") LocalDate fechaHasta);

    @Query("SELECT COUNT(m) FROM MantenimientoActivo m WHERE m.fechaRealizada IS NULL")
    Long countMantenimientosPendientes();

    @Query("SELECT COUNT(m) FROM MantenimientoActivo m WHERE m.fechaRealizada IS NULL AND m.fechaProgramada < CURRENT_DATE")
    Long countMantenimientosAtrasados();

    @Query("SELECT COUNT(m) FROM MantenimientoActivo m WHERE m.fechaRealizada IS NOT NULL AND m.fechaRealizada >= :fechaDesde")
    Long countMantenimientosCompletadosDesde(@Param("fechaDesde") LocalDate fechaDesde);
}