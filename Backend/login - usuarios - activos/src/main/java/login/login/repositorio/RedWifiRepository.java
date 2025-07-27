package login.login.repositorio;

import login.login.entidad.RedWifi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedWifiRepository extends JpaRepository<RedWifi, Integer> {

    // Buscar redes activas
    List<RedWifi> findByActivoTrue();

    // Buscar por nombre de red
    Optional<RedWifi> findByNombreRed(String nombreRed);

    // Buscar por sector
    List<RedWifi> findBySectorIdSector(Integer sectorId);

    // Buscar por tipo de seguridad
    List<RedWifi> findByTipoSeguridad(RedWifi.TipoSeguridad tipoSeguridad);

    // Buscar por zona de ubicación
    List<RedWifi> findByZonaUbicacionContainingIgnoreCase(String zona);

    // Verificar si existe por nombre de red (excluyendo un ID específico)
    boolean existsByNombreRedAndIdWifiNot(String nombreRed, Integer idWifi);

    // Verificar si existe por nombre de red
    boolean existsByNombreRed(String nombreRed);

    // Buscar redes WiFi por múltiples criterios
    @Query("SELECT r FROM RedWifi r WHERE " +
            "(:nombreRed IS NULL OR LOWER(r.nombreRed) LIKE LOWER(CONCAT('%', :nombreRed, '%'))) AND " +
            "(:tipoSeguridad IS NULL OR r.tipoSeguridad = :tipoSeguridad) AND " +
            "(:zona IS NULL OR LOWER(r.zonaUbicacion) LIKE LOWER(CONCAT('%', :zona, '%'))) AND " +
            "(:sectorId IS NULL OR r.sector.idSector = :sectorId) AND " +
            "(:activo IS NULL OR r.activo = :activo)")
    List<RedWifi> findByFiltros(@Param("nombreRed") String nombreRed,
                                @Param("tipoSeguridad") RedWifi.TipoSeguridad tipoSeguridad,
                                @Param("zona") String zona,
                                @Param("sectorId") Integer sectorId,
                                @Param("activo") Boolean activo);

    // Contar redes WiFi
    long count();

    // Contar redes activas
    long countByActivoTrue();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por tipo de seguridad
    long countByTipoSeguridad(RedWifi.TipoSeguridad tipoSeguridad);
}
