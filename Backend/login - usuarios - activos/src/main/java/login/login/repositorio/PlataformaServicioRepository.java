package login.login.repositorio;

import login.login.entidad.PlataformaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlataformaServicioRepository extends JpaRepository<PlataformaServicio, Integer> {

    // Buscar plataformas activas
    List<PlataformaServicio> findByActivoTrue();

    // Buscar por nombre de plataforma
    Optional<PlataformaServicio> findByNombrePlataforma(String nombrePlataforma);

    // Buscar por tipo de servicio
    List<PlataformaServicio> findByTipoServicio(PlataformaServicio.TipoServicio tipoServicio);

    // Buscar por sector
    List<PlataformaServicio> findBySectorIdSector(Integer sectorId);

    // Buscar por email asociado
    List<PlataformaServicio> findByEmailAsociadoContainingIgnoreCase(String email);

    // Buscar por usuario
    List<PlataformaServicio> findByUsuarioContainingIgnoreCase(String usuario);

    // Verificar si existe por nombre de plataforma (excluyendo un ID específico)
    boolean existsByNombrePlataformaIgnoreCaseAndIdPlataformaNot(String nombrePlataforma, Integer idPlataforma);

    // Verificar si existe por nombre de plataforma
    boolean existsByNombrePlataformaIgnoreCase(String nombrePlataforma);

    // Buscar plataformas de servicio por múltiples criterios
    @Query("SELECT p FROM PlataformaServicio p WHERE " +
            "(:nombrePlataforma IS NULL OR LOWER(p.nombrePlataforma) LIKE LOWER(CONCAT('%', :nombrePlataforma, '%'))) AND " +
            "(:tipoServicio IS NULL OR p.tipoServicio = :tipoServicio) AND " +
            "(:usuario IS NULL OR LOWER(p.usuario) LIKE LOWER(CONCAT('%', :usuario, '%'))) AND " +
            "(:emailAsociado IS NULL OR LOWER(p.emailAsociado) LIKE LOWER(CONCAT('%', :emailAsociado, '%'))) AND " +
            "(:tipoCuenta IS NULL OR LOWER(p.tipoCuenta) LIKE LOWER(CONCAT('%', :tipoCuenta, '%'))) AND " +
            "(:sectorId IS NULL OR p.sector.idSector = :sectorId) AND " +
            "(:activo IS NULL OR p.activo = :activo)")
    List<PlataformaServicio> findByFiltros(@Param("nombrePlataforma") String nombrePlataforma,
                                           @Param("tipoServicio") PlataformaServicio.TipoServicio tipoServicio,
                                           @Param("usuario") String usuario,
                                           @Param("emailAsociado") String emailAsociado,
                                           @Param("tipoCuenta") String tipoCuenta,
                                           @Param("sectorId") Integer sectorId,
                                           @Param("activo") Boolean activo);

    // Contar plataformas de servicio
    long count();

    // Contar plataformas activas
    long countByActivoTrue();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por tipo de servicio
    long countByTipoServicio(PlataformaServicio.TipoServicio tipoServicio);
}