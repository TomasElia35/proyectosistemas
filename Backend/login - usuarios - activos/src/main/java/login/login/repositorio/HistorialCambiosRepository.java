package login.login.repositorio;

import login.login.entidad.HistorialCambios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialCambiosRepository extends JpaRepository<HistorialCambios, Integer> {

    // Buscar por tabla afectada
    List<HistorialCambios> findByTablaAfectada(String tablaAfectada);

    // Buscar por registro específico
    List<HistorialCambios> findByTablaAfectadaAndIdRegistro(String tablaAfectada, Integer idRegistro);

    // Buscar por tipo de operación
    List<HistorialCambios> findByTipoOperacion(HistorialCambios.TipoOperacion tipoOperacion);

    // Buscar por usuario
    List<HistorialCambios> findByUsuarioIdUsuario(Integer usuarioId);

    // Buscar por rango de fechas
    List<HistorialCambios> findByFechaCambioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar cambios recientes (últimos N días)
    @Query("SELECT h FROM HistorialCambios h WHERE " +
            "h.fechaCambio >= :fechaDesde " +
            "ORDER BY h.fechaCambio DESC")
    List<HistorialCambios> findCambiosRecientes(@Param("fechaDesde") LocalDateTime fechaDesde);

    // Buscar historial por múltiples criterios
    @Query("SELECT h FROM HistorialCambios h WHERE " +
            "(:tablaAfectada IS NULL OR h.tablaAfectada = :tablaAfectada) AND " +
            "(:idRegistro IS NULL OR h.idRegistro = :idRegistro) AND " +
            "(:tipoOperacion IS NULL OR h.tipoOperacion = :tipoOperacion) AND " +
            "(:usuarioId IS NULL OR h.usuario.idUsuario = :usuarioId) AND " +
            "(:fechaInicio IS NULL OR h.fechaCambio >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR h.fechaCambio <= :fechaFin) " +
            "ORDER BY h.fechaCambio DESC")
    List<HistorialCambios> findByFiltros(@Param("tablaAfectada") String tablaAfectada,
                                         @Param("idRegistro") Integer idRegistro,
                                         @Param("tipoOperacion") HistorialCambios.TipoOperacion tipoOperacion,
                                         @Param("usuarioId") Integer usuarioId,
                                         @Param("fechaInicio") LocalDateTime fechaInicio,
                                         @Param("fechaFin") LocalDateTime fechaFin);

    // Contar cambios por tabla
    long countByTablaAfectada(String tablaAfectada);

    // Contar cambios por usuario
    long countByUsuarioIdUsuario(Integer usuarioId);

    // Contar cambios por tipo de operación
    long countByTipoOperacion(HistorialCambios.TipoOperacion tipoOperacion);

    // Contar cambios en un rango de fechas
    long countByFechaCambioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Obtener las últimas N entradas del historial
    List<HistorialCambios> findTop10ByOrderByFechaCambioDesc();
}