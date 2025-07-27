package login.login.repositorio;

import login.login.entidad.CorreoElectronico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorreoElectronicoRepository extends JpaRepository<CorreoElectronico, Integer> {

    // Buscar correos activos
    List<CorreoElectronico> findByActivoTrue();

    // Buscar por dirección de email
    Optional<CorreoElectronico> findByDireccionEmail(String direccionEmail);

    // Buscar por empleado
    List<CorreoElectronico> findByEmpleadoIdEmpleado(Integer empleadoId);

    // Buscar por sector
    List<CorreoElectronico> findBySectorIdSector(Integer sectorId);

    // Buscar por tipo de cuenta
    List<CorreoElectronico> findByTipoCuenta(CorreoElectronico.TipoCuenta tipoCuenta);

    // Verificar si existe por dirección de email (excluyendo un ID específico)
    boolean existsByDireccionEmailAndIdCorreoNot(String direccionEmail, Integer idCorreo);

    // Verificar si existe por dirección de email
    boolean existsByDireccionEmail(String direccionEmail);

    // Buscar correos electrónicos por múltiples criterios
    @Query("SELECT c FROM CorreoElectronico c WHERE " +
            "(:direccionEmail IS NULL OR LOWER(c.direccionEmail) LIKE LOWER(CONCAT('%', :direccionEmail, '%'))) AND " +
            "(:tipoCuenta IS NULL OR c.tipoCuenta = :tipoCuenta) AND " +
            "(:empleadoId IS NULL OR c.empleado.idEmpleado = :empleadoId) AND " +
            "(:sectorId IS NULL OR c.sector.idSector = :sectorId) AND " +
            "(:activo IS NULL OR c.activo = :activo)")
    List<CorreoElectronico> findByFiltros(@Param("direccionEmail") String direccionEmail,
                                          @Param("tipoCuenta") CorreoElectronico.TipoCuenta tipoCuenta,
                                          @Param("empleadoId") Integer empleadoId,
                                          @Param("sectorId") Integer sectorId,
                                          @Param("activo") Boolean activo);

    // Contar correos electrónicos
    long count();

    // Contar correos activos
    long countByActivoTrue();

    // Contar por empleado
    long countByEmpleadoIdEmpleado(Integer empleadoId);

    // Contar por sector
    long countBySectorIdSector(Integer sectorId);

    // Contar por tipo de cuenta
    long countByTipoCuenta(CorreoElectronico.TipoCuenta tipoCuenta);
}