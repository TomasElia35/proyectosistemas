package login.login.repositorio;

import login.login.entidad.EquipoEnergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoEnergiaRepository extends JpaRepository<EquipoEnergia, Integer> {

    // Buscar por código de activo
    Optional<EquipoEnergia> findByCodigoActivo(String codigoActivo);

    // Buscar por sector
    List<EquipoEnergia> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<EquipoEnergia> findByEstadoIdEstado(Integer estadoId);

    // Buscar por tipo
    List<EquipoEnergia> findByTipo(EquipoEnergia.TipoEquipoEnergia tipo);

    // Buscar por número de serie
    Optional<EquipoEnergia> findByNumeroSerie(String numeroSerie);

    // Buscar por ubicación física
    List<EquipoEnergia> findByUbicacionFisicaContainingIgnoreCase(String ubicacion);

    // Buscar por potencia
    List<EquipoEnergia> findByPotenciaContainingIgnoreCase(String potencia);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdEquipoEnergiaNot(String codigoActivo, Integer idEquipoEnergia);

    // Verificar si existe por número de serie (excluyendo un ID específico)
    boolean existsByNumeroSerieAndIdEquipoEnergiaNot(String numeroSerie, Integer idEquipoEnergia);

    // Buscar equipos de energía por múltiples criterios
    @Query("SELECT e FROM EquipoEnergia e WHERE " +
            "(:codigoActivo IS NULL OR LOWER(e.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:tipo IS NULL OR e.tipo = :tipo) AND " +
            "(:marca IS NULL OR LOWER(e.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(e.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:potencia IS NULL OR LOWER(e.potencia) LIKE LOWER(CONCAT('%', :potencia, '%'))) AND " +
            "(:ubicacion IS NULL OR LOWER(e.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) AND " +
            "(:sectorId IS NULL OR e.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR e.estado.idEstado = :estadoId)")
    List<EquipoEnergia> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                      @Param("tipo") EquipoEnergia.TipoEquipoEnergia tipo,
                                      @Param("marca") String marca,
                                      @Param("modelo") String modelo,
                                      @Param("potencia") String potencia,
                                      @Param("ubicacion") String ubicacion,
                                      @Param("sectorId") Integer sectorId,
                                      @Param("estadoId") Integer estadoId);

    // Contar equipos de energía
    long count();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);

    // Contar por tipo
    long countByTipo(EquipoEnergia.TipoEquipoEnergia tipo);
}