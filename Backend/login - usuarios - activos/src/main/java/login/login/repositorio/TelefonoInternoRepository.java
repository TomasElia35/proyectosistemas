package login.login.repositorio;

import login.login.entidad.TelefonoInterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefonoInternoRepository extends JpaRepository<TelefonoInterno, Integer> {

    // Buscar por código de activo
    Optional<TelefonoInterno> findByCodigoActivo(String codigoActivo);

    // Buscar por número interno
    Optional<TelefonoInterno> findByNumeroInterno(String numeroInterno);

    // Buscar por empleado
    List<TelefonoInterno> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<TelefonoInterno> findBySectorIdSector(Integer sectorId);

    // Buscar por estado
    List<TelefonoInterno> findByEstadoIdEstado(Integer estadoId);

    // Buscar por ubicación física
    List<TelefonoInterno> findByUbicacionFisicaContainingIgnoreCase(String ubicacion);

    // Verificar si existe por código de activo (excluyendo un ID específico)
    boolean existsByCodigoActivoAndIdTelefonoNot(String codigoActivo, Integer idTelefono);

    // Verificar si existe por número interno (excluyendo un ID específico)
    boolean existsByNumeroInternoAndIdTelefonoNot(String numeroInterno, Integer idTelefono);

    // Buscar teléfonos por múltiples criterios
    @Query("SELECT t FROM TelefonoInterno t WHERE " +
            "(:codigoActivo IS NULL OR LOWER(t.codigoActivo) LIKE LOWER(CONCAT('%', :codigoActivo, '%'))) AND " +
            "(:numeroInterno IS NULL OR t.numeroInterno = :numeroInterno) AND " +
            "(:marca IS NULL OR LOWER(t.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(t.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:ubicacion IS NULL OR LOWER(t.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) AND " +
            "(:empleadoId IS NULL OR t.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR t.sector.idSector = :sectorId) AND " +
            "(:estadoId IS NULL OR t.estado.idEstado = :estadoId)")
    List<TelefonoInterno> findByFiltros(@Param("codigoActivo") String codigoActivo,
                                        @Param("numeroInterno") String numeroInterno,
                                        @Param("marca") String marca,
                                        @Param("modelo") String modelo,
                                        @Param("ubicacion") String ubicacion,
                                        @Param("empleadoId") Integer empleadoId,
                                        @Param("sectorId") Integer sectorId,
                                        @Param("estadoId") Integer estadoId);

    // Contar teléfonos
    long count();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por estado
    long countByEstadoIdEstado(Integer estadoId);
}