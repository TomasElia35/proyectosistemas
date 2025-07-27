package login.login.repositorio;

import login.login.entidad.Scanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScannerRepository extends JpaRepository<Scanner, Integer> {

    // Buscar por código de activo
    Optional<Scanner> findByCodigoActivo(String codigoActivo);

    // Buscar por empleado
    List<Scanner> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<Scanner> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<Scanner> findByEstadoIdEstado(Integer estadoId);

    // Buscar por dirección IP
    Optional<Scanner> findByDireccionIp(String direccionIp);

    // Buscar por número de serie
    Optional<Scanner> findByNumeroSerie(String numeroSerie);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdScannerNot(String codigoActivo, Integer idScanner);

    // Verificar si existe por dirección IP (excluyendo un ID específico)
    boolean existsByDireccionIpAndIdScannerNot(String direccionIp, Integer idScanner);

    // Verificar si existe por número de serie (excluyendo un ID específico)
    boolean existsByNumeroSerieAndIdScannerNot(String numeroSerie, Integer idScanner);

    // Buscar scanners por múltiples criterios
    @Query("SELECT s FROM Scanner s WHERE " +
            "(:codigoActivo IS NULL OR LOWER(s.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:marca IS NULL OR LOWER(s.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(s.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:direccionIp IS NULL OR s.direccionIp = :direccionIp) AND " +
            "(:empleadoId IS NULL OR s.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR s.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR s.estado.idEstado = :estadoId)")
    List<Scanner> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                @Param("marca") String marca,
                                @Param("modelo") String modelo,
                                @Param("direccionIp") String direccionIp,
                                @Param("empleadoId") Integer empleadoId,
                                @Param("sectorId") Integer sectorId,
                                @Param("estadoId") Integer estadoId);

    // Contar scanners
    long count();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);
}