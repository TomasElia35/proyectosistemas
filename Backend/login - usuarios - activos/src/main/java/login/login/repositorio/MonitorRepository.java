package login.login.repositorio;

import login.login.entidad.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Integer> {

    // Buscar por código de activo
    Optional<Monitor> findByCodigoActivo(String codigoActivo);

    // Buscar por empleado
    List<Monitor> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<Monitor> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<Monitor> findByEstadoIdEstado(Integer estadoId);

    // Buscar por número de serie
    Optional<Monitor> findByNumeroSerie(String numeroSerie);

    // Buscar por tamaño
    List<Monitor> findByTamano(String tamano);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdMonitorNot(String codigoActivo, Integer idMonitor);

    // Verificar si existe por número de serie (excluyendo un ID específico)
    boolean existsByNumeroSerieAndIdMonitorNot(String numeroSerie, Integer idMonitor);

    // Buscar monitores por múltiples criterios
    @Query("SELECT m FROM Monitor m WHERE " +
            "(:codigoActivo IS NULL OR LOWER(m.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:marca IS NULL OR LOWER(m.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(m.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:tamano IS NULL OR m.tamano = :tamano) AND " +
            "(:empleadoId IS NULL OR m.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR m.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR m.estado.idEstado = :estadoId)")
    List<Monitor> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                @Param("marca") String marca,
                                @Param("modelo") String modelo,
                                @Param("tamano") String tamano,
                                @Param("empleadoId") Integer empleadoId,
                                @Param("sectorId") Integer sectorId,
                                @Param("estadoId") Integer estadoId);

    // Contar monitores
    long count();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);
}