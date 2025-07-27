package login.login.repositorio;

import login.login.entidad.DispositivoAlmacenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoAlmacenamientoRepository extends JpaRepository<DispositivoAlmacenamiento, Integer> {

    // Buscar por código de activo
    Optional<DispositivoAlmacenamiento> findByCodigoActivo(String codigoActivo);

    // Buscar por empleado
    List<DispositivoAlmacenamiento> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<DispositivoAlmacenamiento> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<DispositivoAlmacenamiento> findByEstadoIdEstado(Integer estadoId);

    // Buscar por tipo
    List<DispositivoAlmacenamiento> findByTipo(DispositivoAlmacenamiento.TipoAlmacenamiento tipo);

    // Buscar por capacidad
    List<DispositivoAlmacenamiento> findByCapacidaContainingIgnoreCase(String capacidad);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdAlmacenamientoNot(String codigoActivo, Integer idAlmacenamiento);

    // Buscar dispositivos de almacenamiento por múltiples criterios
    @Query("SELECT d FROM DispositivoAlmacenamiento d WHERE " +
            "(:codigoActivo IS NULL OR LOWER(d.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:tipo IS NULL OR d.tipo = :tipo) AND " +
            "(:marca IS NULL OR LOWER(d.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(d.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:capacidad IS NULL OR LOWER(d.capacidad) LIKE LOWER(CONCAT('%', :capacidad, '%'))) AND " +
            "(:empleadoId IS NULL OR d.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR d.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR d.estado.idEstado = :estadoId)")
    List<DispositivoAlmacenamiento> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                                  @Param("tipo") DispositivoAlmacenamiento.TipoAlmacenamiento tipo,
                                                  @Param("marca") String marca,
                                                  @Param("modelo") String modelo,
                                                  @Param("capacidad") String capacidad,
                                                  @Param("empleadoId") Integer empleadoId,
                                                  @Param("sectorId") Integer sectorId,
                                                  @Param("estadoId") Integer estadoId);

    // Contar dispositivos de almacenamiento
    long count();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);

    // Contar por tipo
    long countByTipo(DispositivoAlmacenamiento.TipoAlmacenamiento tipo);
}