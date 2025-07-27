package login.login.repositorio;

import login.login.entidad.ControladorCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ControladorCajaRepository extends JpaRepository<ControladorCaja, Integer> {

    // Buscar controladores activos
    List<ControladorCaja> findByActivoTrue();

    // Buscar por código de controlador
    Optional<ControladorCaja> findByCodigoControlador(String codigoControlador);

    // Buscar por dirección IP
    Optional<ControladorCaja> findByDireccionIp(String direccionIp);

    // Buscar por sector
    List<ControladorCaja> findBySectorIdSector(Integer sectorId);

    // Buscar por ubicación física
    List<ControladorCaja> findByUbicacionFisicaContainingIgnoreCase(String ubicacion);

    // Verificar si existe por código de controlador (excluyendo un ID específico)
    boolean existsByCodigoControladorAndIdControladorNot(String codigoControlador, Integer idControlador);

    // Verificar si existe por dirección IP (excluyendo un ID específico)
    boolean existsByDireccionIpAndIdControladorNot(String direccionIp, Integer idControlador);

    // Verificar si existe por código de controlador
    boolean existsByCodigoControlador(String codigoControlador);

    // Verificar si existe por dirección IP
    boolean existsByDireccionIp(String direccionIp);

    // Buscar controladores de caja por múltiples criterios
    @Query("SELECT c FROM ControladorCaja c WHERE " +
            "(:codigoControlador IS NULL OR LOWER(c.codigoControlador) LIKE LOWER(CONCAT('%', :codigoControlador, '%'))) AND " +
            "(:direccionIp IS NULL OR c.direccionIp = :direccionIp) AND " +
            "(:ubicacion IS NULL OR LOWER(c.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) AND " +
            "(:sectorId IS NULL OR c.sector.idSector = :sectorId) AND " +
            "(:activo IS NULL OR c.activo = :activo)")
    List<ControladorCaja> findByFiltros(@Param("codigoControlador") String codigoControlador,
                                        @Param("direccionIp") String direccionIp,
                                        @Param("ubicacion") String ubicacion,
                                        @Param("sectorId") Integer sectorId,
                                        @Param("activo") Boolean activo);

    // Contar controladores de caja
    long count();

    // Contar controladores activos
    long countByActivoTrue();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);
}