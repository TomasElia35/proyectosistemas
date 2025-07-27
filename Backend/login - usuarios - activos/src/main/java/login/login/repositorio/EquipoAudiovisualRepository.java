package login.login.repositorio;

import login.login.entidad.EquipoAudiovisual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoAudiovisualRepository extends JpaRepository<EquipoAudiovisual, Integer> {

    // Buscar por código de activo
    Optional<EquipoAudiovisual> findByCodigoActivo(String codigoActivo);

    // Buscar por sector
    List<EquipoAudiovisual> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<EquipoAudiovisual> findByEstadoIdEstado(Integer estadoId);

    // Buscar por tipo
    List<EquipoAudiovisual> findByTipo(EquipoAudiovisual.TipoEquipoAudiovisual tipo);

    // Buscar por número de serie
    Optional<EquipoAudiovisual> findByNumeroSerie(String numeroSerie);

    // Buscar por ubicación física
    List<EquipoAudiovisual> findByUbicacionFisicaContainingIgnoreCase(String ubicacion);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdEquipoAVNot(String codigoActivo, Integer idEquipoAV);

    // Verificar si existe por número de serie (excluyendo un ID específico)
    boolean existsByNumeroSerieAndIdEquipoAVNot(String numeroSerie, Integer idEquipoAV);

    // Buscar equipos audiovisuales por múltiples criterios
    @Query("SELECT e FROM EquipoAudiovisual e WHERE " +
            "(:codigoActivo IS NULL OR LOWER(e.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:tipo IS NULL OR e.tipo = :tipo) AND " +
            "(:marca IS NULL OR LOWER(e.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(e.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:ubicacion IS NULL OR LOWER(e.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) AND " +
            "(:sectorId IS NULL OR e.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR e.estado.idEstado = :estadoId)")
    List<EquipoAudiovisual> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                          @Param("tipo") EquipoAudiovisual.TipoEquipoAudiovisual tipo,
                                          @Param("marca") String marca,
                                          @Param("modelo") String modelo,
                                          @Param("ubicacion") String ubicacion,
                                          @Param("sectorId") Integer sectorId,
                                          @Param("estadoId") Integer estadoId);

    // Contar equipos audiovisuales
    long count();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);

    // Contar por tipo
    long countByTipo(EquipoAudiovisual.TipoEquipoAudiovisual tipo);
}