package login.login.repositorio;

import login.login.entidad.Computadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComputadoraRepository extends JpaRepository<Computadora, Integer> {

    // Buscar por código de activo
    Optional<Computadora> findByCodigoActivo(String codigoActivo);

    // Buscar por empleado
    List<Computadora> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<Computadora> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<Computadora> findByEstadoIdEstado(Integer estadoId);

    // Buscar por dirección IP
    Optional<Computadora> findByDireccionIp(String direccionIp);

    // Buscar por número de serie
    Optional<Computadora> findByNumeroSerie(String numeroSerie);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdComputadoraNot(String codigoActivo, Integer idComputadora);

    // Verificar si existe por dirección IP (excluyendo un ID específico)
    boolean existsByDireccionIpAndIdComputadoraNot(String direccionIp, Integer idComputadora);

    // Verificar si existe por número de serie (excluyendo un ID específico)
    boolean existsByNumeroSerieAndIdComputadoraNot(String numeroSerie, Integer idComputadora);

    // Verificar existencia por código de activo
    boolean existsByCodigoActivo(String codigoActivo);

    // Verificar existencia por dirección IP
    boolean existsByDireccionIp(String direccionIp);

    // Verificar existencia por número de serie
    boolean existsByNumeroSerie(String numeroSerie);

    // Buscar computadoras por múltiples criterios
    @Query("SELECT c FROM Computadora c WHERE " +
            "(:codigoActivo IS NULL OR LOWER(c.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:marca IS NULL OR LOWER(c.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(c.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:direccionIp IS NULL OR c.direccionIp = :direccionIp) AND " +
            "(:empleadoId IS NULL OR c.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR c.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR c.estado.idEstado = :estadoId)")
    List<Computadora> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                    @Param("marca") String marca,
                                    @Param("modelo") String modelo,
                                    @Param("direccionIp") String direccionIp,
                                    @Param("empleadoId") Integer empleadoId,
                                    @Param("sectorId") Integer sectorId,
                                    @Param("estadoId") Integer estadoId);

    // Contar computadoras
    long count();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);
}