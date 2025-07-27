package login.login.repositorio;

import login.login.entidad.DispositivoMovil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoMovilRepository extends JpaRepository<DispositivoMovil, Integer> {

    // Buscar por código de activo
    Optional<DispositivoMovil> findByCodigoActivo(String codigoActivo);

    // Buscar por empleado
    List<DispositivoMovil> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<DispositivoMovil> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<DispositivoMovil> findByEstadoIdEstado(Integer estadoId);

    // Buscar por IMEI
    Optional<DispositivoMovil> findByImei(String imei);

    // Buscar por número de teléfono
    Optional<DispositivoMovil> findByNumeroTelefono(String numeroTelefono);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdDispositivoNot(String codigoActivo, Integer idDispositivo);

    // Verificar si existe por IMEI (excluyendo un ID específico)
    boolean existsByImeiAndIdDispositivoNot(String imei, Integer idDispositivo);

    // Buscar dispositivos por múltiples criterios
    @Query("SELECT d FROM DispositivoMovil d WHERE " +
            "(:codigoActivo IS NULL OR LOWER(d.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:marca IS NULL OR LOWER(d.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(d.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:empleadoId IS NULL OR d.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR d.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR d.estado.idEstado = :estadoId)")
    List<DispositivoMovil> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                         @Param("marca") String marca,
                                         @Param("modelo") String modelo,
                                         @Param("empleadoId") Integer empleadoId,
                                         @Param("sectorId") Integer sectorId,
                                         @Param("estadoId") Integer estadoId);

    // Contar dispositivos móviles
    long count();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);
}