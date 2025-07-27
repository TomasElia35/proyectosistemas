package login.login.repositorio;

import login.login.entidad.SistemaEmpresarial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SistemaEmpresarialRepository extends JpaRepository<SistemaEmpresarial, Integer> {

    // Buscar sistemas activos
    List<SistemaEmpresarial> findByActivoTrue();

    // Buscar por nombre de sistema
    Optional<SistemaEmpresarial> findByNombreSistema(String nombreSistema);

    // Buscar por tipo de sistema
    List<SistemaEmpresarial> findByTipoSistema(SistemaEmpresarial.TipoSistema tipoSistema);

    // Buscar por empleado
    List<SistemaEmpresarial> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<SistemaEmpresarial> findBySectorIdSector(Integer sectorId);

    // Buscar por usuario
    List<SistemaEmpresarial> findByUsuarioContainingIgnoreCase(String usuario);

    // Buscar por URL de acceso
    List<SistemaEmpresarial> findByUrlAccesoContainingIgnoreCase(String urlAcceso);

    // Verificar si existe por nombre de sistema (excluyendo un ID específico)
    boolean existsByNombreSistemaIgnoreCaseAndIdSistemaNot(String nombreSistema, Integer idSistema);

    // Verificar si existe por nombre de sistema
    boolean existsByNombreSistemaIgnoreCase(String nombreSistema);

    // Buscar sistemas empresariales por múltiples criterios
    @Query("SELECT s FROM SistemaEmpresarial s WHERE " +
            "(:nombreSistema IS NULL OR LOWER(s.nombreSistema) LIKE LOWER(CONCAT('%', :nombreSistema, '%'))) AND " +
            "(:tipoSistema IS NULL OR s.tipoSistema = :tipoSistema) AND " +
            "(:usuario IS NULL OR LOWER(s.usuario) LIKE LOWER(CONCAT('%', :usuario, '%'))) AND " +
            "(:urlAcceso IS NULL OR LOWER(s.urlAcceso) LIKE LOWER(CONCAT('%', :urlAcceso, '%'))) AND " +
            "(:rolPermisos IS NULL OR LOWER(s.rolPermisos) LIKE LOWER(CONCAT('%', :rolPermisos, '%'))) AND " +
            "(:empleadoId IS NULL OR s.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR s.sector.idSector = :sectorId) AND " +
            "(:activo IS NULL OR s.activo = :activo)")
    List<SistemaEmpresarial> findByFiltros(@Param("nombreSistema") String nombreSistema,
                                           @Param("tipoSistema") SistemaEmpresarial.TipoSistema tipoSistema,
                                           @Param("usuario") String usuario,
                                           @Param("urlAcceso") String urlAcceso,
                                           @Param("rolPermisos") String rolPermisos,
                                           @Param("empleadoId") Integer empleadoId,
                                           @Param("sectorId") Integer sectorId,
                                           @Param("activo") Boolean activo);

    // Contar sistemas empresariales
    long count();

    // Contar sistemas activos
    long countByActivoTrue();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por tipo de sistema
    long countByTipoSistema(SistemaEmpresarial.TipoSistema tipoSistema);
}