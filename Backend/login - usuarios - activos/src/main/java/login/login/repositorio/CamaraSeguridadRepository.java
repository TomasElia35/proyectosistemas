package login.login.repositorio;

import login.login.entidad.CamaraSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CamaraSeguridadRepository extends JpaRepository<CamaraSeguridad, Integer> {

    // Buscar por código de activo
    Optional<CamaraSeguridad> findByCodigoActivo(String codigoActivo);

    // Buscar por sector
    List<CamaraSeguridad> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<CamaraSeguridad> findByEstadoIdEstado(Integer estadoId);

    // Buscar por dirección IP
    Optional<CamaraSeguridad> findByDireccionIp(String direccionIp);

    // Buscar por ubicación física
    List<CamaraSeguridad> findByUbicacionFisicaContainingIgnoreCase(String ubicacion);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdCamaraNot(String codigoActivo, Integer idCamara);

    // Verificar si existe por dirección IP (excluyendo un ID específico)
    boolean existsByDireccionIpAndIdCamaraNot(String direccionIp, Integer idCamara);

    // Buscar cámaras de seguridad por múltiples criterios
    @Query("SELECT c FROM CamaraSeguridad c WHERE " +
            "(:codigoActivo IS NULL OR LOWER(c.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:marca IS NULL OR LOWER(c.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(c.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:direccionIp IS NULL OR c.direccionIp = :direccionIp) AND " +
            "(:ubicacion IS NULL OR LOWER(c.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) AND " +
            "(:sectorId IS NULL OR c.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR c.estado.idEstado = :estadoId)")
    List<CamaraSeguridad> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                        @Param("marca") String marca,
                                        @Param("modelo") String modelo,
                                        @Param("direccionIp") String direccionIp,
                                        @Param("ubicacion") String ubicacion,
                                        @Param("sectorId") Integer sectorId,
                                        @Param("estadoId") Integer estadoId);

    // Contar cámaras de seguridad
    long count();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);
}