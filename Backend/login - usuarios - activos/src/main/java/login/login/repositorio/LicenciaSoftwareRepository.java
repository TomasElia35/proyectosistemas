package login.login.repositorio;

import login.login.entidad.LicenciaSoftware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LicenciaSoftwareRepository extends JpaRepository<LicenciaSoftware, Integer> {

    // Buscar licencias activas
    List<LicenciaSoftware> findByActivoTrue();

    // Buscar por empleado
    List<LicenciaSoftware> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<LicenciaSoftware> findBySectorIdSector(Integer sectorId);

    // Buscar por tipo de software
    List<LicenciaSoftware> findByTipoSoftware(LicenciaSoftware.TipoSoftware tipoSoftware);

    // Buscar por tipo de licencia
    List<LicenciaSoftware> findByTipoLicencia(LicenciaSoftware.TipoLicencia tipoLicencia);

    // Buscar por nombre de software
    List<LicenciaSoftware> findByNombreSoftwareContainingIgnoreCase(String nombreSoftware);

    // Buscar por clave de producto
    Optional<LicenciaSoftware> findByClaveProducto(String claveProducto);

    // Buscar por clave de licencia
    Optional<LicenciaSoftware> findByClaveLicencia(String claveLicencia);

    // Buscar por email asociado
    List<LicenciaSoftware> findByEmailAsociadoContainingIgnoreCase(String emailAsociado);

    // Buscar licencias próximas a vencer (en los próximos N días)
    @Query("SELECT l FROM LicenciaSoftware l WHERE " +
            "l.fechaVencimiento IS NOT NULL AND " +
            "l.fechaVencimiento BETWEEN CURRENT_DATE AND :fechaLimite AND " +
            "l.activo = true")
    List<LicenciaSoftware> findLicenciasProximasAVencer(@Param("fechaLimite") LocalDate fechaLimite);

    // Buscar licencias vencidas
    @Query("SELECT l FROM LicenciaSoftware l WHERE " +
            "l.fechaVencimiento IS NOT NULL AND " +
            "l.fechaVencimiento < CURRENT_DATE AND " +
            "l.activo = true")
    List<LicenciaSoftware> findLicenciasVencidas();

    // Verificar si existe por clave de producto (excluyendo un ID específico)
    boolean existsByClaveProductoAndIdLicenciaNot(String claveProducto, Integer idLicencia);

    // Verificar si existe por clave de licencia (excluyendo un ID específico)
    boolean existsByClaveLicenciaAndIdLicenciaNot(String claveLicencia, Integer idLicencia);

    // Buscar licencias de software por múltiples criterios
    @Query("SELECT l FROM LicenciaSoftware l WHERE " +
            "(:nombreSoftware IS NULL OR LOWER(l.nombreSoftware) LIKE LOWER(CONCAT('%', :nombreSoftware, '%'))) AND " +
            "(:tipoSoftware IS NULL OR l.tipoSoftware = :tipoSoftware) AND " +
            "(:tipoLicencia IS NULL OR l.tipoLicencia = :tipoLicencia) AND " +
            "(:version IS NULL OR LOWER(l.version) LIKE LOWER(CONCAT('%', :version, '%'))) AND " +
            "(:emailAsociado IS NULL OR LOWER(l.emailAsociado) LIKE LOWER(CONCAT('%', :emailAsociado, '%'))) AND " +
            "(:proveedor IS NULL OR LOWER(l.proveedor) LIKE LOWER(CONCAT('%', :proveedor, '%'))) AND " +
            "(:empleadoId IS NULL OR l.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR l.sector.idSector = :sectorId) AND " +
            "(:activo IS NULL OR l.activo = :activo)")
    List<LicenciaSoftware> findByFiltros(@Param("nombreSoftware") String nombreSoftware,
                                         @Param("tipoSoftware") LicenciaSoftware.TipoSoftware tipoSoftware,
                                         @Param("tipoLicencia") LicenciaSoftware.TipoLicencia tipoLicencia,
                                         @Param("version") String version,
                                         @Param("emailAsociado") String emailAsociado,
                                         @Param("proveedor") String proveedor,
                                         @Param("empleadoId") Integer empleadoId,
                                         @Param("sectorId") Integer sectorId,
                                         @Param("activo") Boolean activo);

    // Contar licencias de software
    long count();

    // Contar licencias activas
    long countByActivoTrue();

    // Contar licencias vencidas
    @Query("SELECT COUNT(l) FROM LicenciaSoftware l WHERE " +
            "l.fechaVencimiento IS NOT NULL AND " +
            "l.fechaVencimiento < CURRENT_DATE AND " +
            "l.activo = true")
    long countLicenciasVencidas();

    // Contar licencias próximas a vencer
    @Query("SELECT COUNT(l) FROM LicenciaSoftware l WHERE " +
            "l.fechaVencimiento IS NOT NULL AND " +
            "l.fechaVencimiento BETWEEN CURRENT_DATE AND :fechaLimite AND " +
            "l.activo = true")
    long countLicenciasProximasAVencer(@Param("fechaLimite") LocalDate fechaLimite);

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por tipo de software
    long countByTipoSoftware(LicenciaSoftware.TipoSoftware tipoSoftware);

    // Contar por tipo de licencia
    long countByTipoLicencia(LicenciaSoftware.TipoLicencia tipoLicencia);
}