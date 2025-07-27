package login.login.repositorio;

import login.login.entidad.Impresora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpresoraRepository extends JpaRepository<Impresora, Integer> {

    // Buscar por código de activo
    Optional<Impresora> findByCodigoActivo(String codigoActivo);

    // Buscar por sector
    List<Impresora> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<Impresora> findByEstadoIdEstado(Integer estadoId);

    // Buscar por dirección IP
    Optional<Impresora> findByDireccionIp(String direccionIp);

    // Buscar por número de serie
    Optional<Impresora> findByNumeroSerie(String numeroSerie);

    // Buscar por ubicación física
    List<Impresora> findByUbicacionFisicaContainingIgnoreCase(String ubicacion);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdImpresoraNot(String codigoActivo, Integer idImpresora);

    // Verificar si existe por dirección IP (excluyendo un ID específico)
    boolean existsByDireccionIpAndIdImpresoraNot(String direccionIp, Integer idImpresora);

    // Verificar si existe por número de serie (excluyendo un ID específico)
    boolean existsByNumeroSerieAndIdImpresoraNot(String numeroSerie, Integer idImpresora);

    // Buscar impresoras por múltiples criterios
    @Query("SELECT i FROM Impresora i WHERE " +
            "(:codigoActivo IS NULL OR LOWER(i.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:marca IS NULL OR LOWER(i.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(i.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:direccionIp IS NULL OR i.direccionIp = :direccionIp) AND " +
            "(:ubicacion IS NULL OR LOWER(i.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) AND " +
            "(:sectorId IS NULL OR i.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR i.estado.idEstado = :estadoId)")
    List<Impresora> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                  @Param("marca") String marca,
                                  @Param("modelo") String modelo,
                                  @Param("direccionIp") String direccionIp,
                                  @Param("ubicacion") String ubicacion,
                                  @Param("sectorId") Integer sectorId,
                                  @Param("estadoId") Integer estadoId);

    // Contar impresoras
    long count();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);
}