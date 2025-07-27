package login.login.repositorio;

import login.login.entidad.EquipoRed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRedRepository extends JpaRepository<EquipoRed, Integer> {

    // Buscar por código de activo
    Optional<EquipoRed> findByCodigoActivo(String codigoActivo);

    // Buscar por sector
    List<EquipoRed> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<EquipoRed> findByEstadoIdEstado(Integer estadoId);

    // Buscar por tipo
    List<EquipoRed> findByTipo(EquipoRed.TipoEquipoRed tipo);

    // Buscar por dirección IP
    Optional<EquipoRed> findByDireccionIp(String direccionIp);

    // Buscar por número de serie
    Optional<EquipoRed> findByNumeroSerie(String numeroSerie);

    // Buscar por ubicación física
    List<EquipoRed> findByUbicacionFisicaContainingIgnoreCase(String ubicacion);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdEquipoRedNot(String codigoActivo, Integer idEquipoRed);

    // Verificar si existe por dirección IP (excluyendo un ID específico)
    boolean existsByDireccionIpAndIdEquipoRedNot(String direccionIp, Integer idEquipoRed);

    // Verificar si existe por número de serie (excluyendo un ID específico)
    boolean existsByNumeroSerieAndIdEquipoRedNot(String numeroSerie, Integer idEquipoRed);

    // Buscar equipos de red por múltiples criterios
    @Query("SELECT e FROM EquipoRed e WHERE " +
            "(:codigoActivo IS NULL OR LOWER(e.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:tipo IS NULL OR e.tipo = :tipo) AND " +
            "(:marca IS NULL OR LOWER(e.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(e.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:direccionIp IS NULL OR e.direccionIp = :direccionIp) AND " +
            "(:ubicacion IS NULL OR LOWER(e.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) AND " +
            "(:sectorId IS NULL OR e.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR e.estado.idEstado = :estadoId)")
    List<EquipoRed> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                  @Param("tipo") EquipoRed.TipoEquipoRed tipo,
                                  @Param("marca") String marca,
                                  @Param("modelo") String modelo,
                                  @Param("direccionIp") String direccionIp,
                                  @Param("ubicacion") String ubicacion,
                                  @Param("sectorId") Integer sectorId,
                                  @Param("estadoId") Integer estadoId);

    // Contar equipos de red
    long count();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);

    // Contar por tipo
    long countByTipo(EquipoRed.TipoEquipoRed tipo);
}