package login.login.repositorio;

import login.login.entidad.ServidorFtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServidorFtpRepository extends JpaRepository<ServidorFtp, Integer> {

    // Buscar servidores activos
    List<ServidorFtp> findByActivoTrue();

    // Buscar por nombre de servidor
    Optional<ServidorFtp> findByNombreServidor(String nombreServidor);

    // Buscar por dirección IP
    Optional<ServidorFtp> findByDireccionIp(String direccionIp);

    // Buscar por URL de servidor
    Optional<ServidorFtp> findByUrlServidor(String urlServidor);

    // Buscar por sector
    List<ServidorFtp> findBySectorIdSector(Integer sectorId);

    // Buscar por puerto
    List<ServidorFtp> findByPuerto(Integer puerto);

    // Verificar si existe por nombre de servidor (excluyendo un ID específico)
    boolean existsByNombreServidorIgnoreCaseAndIdServidorNot(String nombreServidor, Integer idServidor);

    // Verificar si existe por dirección IP (excluyendo un ID específico)
    boolean existsByDireccionIpAndIdServidorNot(String direccionIp, Integer idServidor);

    // Verificar si existe por URL de servidor (excluyendo un ID específico)
    boolean existsByUrlServidorAndIdServidorNot(String urlServidor, Integer idServidor);

    // Verificar si existe por nombre de servidor
    boolean existsByNombreServidorIgnoreCase(String nombreServidor);

    // Verificar si existe por dirección IP
    boolean existsByDireccionIp(String direccionIp);

    // Buscar servidores FTP por múltiples criterios
    @Query("SELECT s FROM ServidorFtp s WHERE " +
            "(:nombreServidor IS NULL OR LOWER(s.nombreServidor) LIKE LOWER(CONCAT('%', :nombreServidor, '%'))) AND " +
            "(:direccionIp IS NULL OR s.direccionIp = :direccionIp) AND " +
            "(:urlServidor IS NULL OR LOWER(s.urlServidor) LIKE LOWER(CONCAT('%', :urlServidor, '%'))) AND " +
            "(:puerto IS NULL OR s.puerto = :puerto) AND " +
            "(:usuario IS NULL OR LOWER(s.usuario) LIKE LOWER(CONCAT('%', :usuario, '%'))) AND " +
            "(:sectorId IS NULL OR s.sector.idSector = :sectorId) AND " +
            "(:activo IS NULL OR s.activo = :activo)")
    List<ServidorFtp> findByFiltros(@Param("nombreServidor") String nombreServidor,
                                    @Param("direccionIp") String direccionIp,
                                    @Param("urlServidor") String urlServidor,
                                    @Param("puerto") Integer puerto,
                                    @Param("usuario") String usuario,
                                    @Param("sectorId") Integer sectorId,
                                    @Param("activo") Boolean activo);

    // Contar servidores FTP
    long count();

    // Contar servidores activos
    long countByActivoTrue();

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);
}