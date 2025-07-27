package login.login.repositorio;

import login.login.entidad.Periferico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerifericoRepository extends JpaRepository<Periferico, Integer> {

    // Buscar por código de activo
    Optional<Periferico> findByCodigoActivo(String codigoActivo);

    // Buscar por empleado
    List<Periferico> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<Periferico> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<Periferico> findByEstadoIdEstado(Integer estadoId);

    // Buscar por tipo
    List<Periferico> findByTipo(Periferico.TipoPeriferico tipo);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdPerifericoNot(String codigoActivo, Integer idPeriferico);

    // Buscar periféricos por múltiples criterios
    @Query("SELECT p FROM Periferico p WHERE " +
            "(:codigoActivo IS NULL OR LOWER(p.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:tipo IS NULL OR p.tipo = :tipo) AND " +
            "(:marca IS NULL OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(p.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:empleadoId IS NULL OR p.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR p.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR p.estado.idEstado = :estadoId)")
    List<Periferico> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                   @Param("tipo") Periferico.TipoPeriferico tipo,
                                   @Param("marca") String marca,
                                   @Param("modelo") String modelo,
                                   @Param("empleadoId") Integer empleadoId,
                                   @Param("sectorId") Integer sectorId,
                                   @Param("estadoId") Integer estadoId);

    // Contar periféricos
    long count();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);

    // Contar por tipo
    long countByTipo(Periferico.TipoPeriferico tipo);
}