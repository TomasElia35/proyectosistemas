package login.login.repositorio;

import login.login.entidad.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    // Buscar empleados activos
    List<Empleado> findByActivoTrue();

    // Buscar por sector
    List<Empleado> findBySectorIdSectorAndActivoTrue(Integer sectorId);

    // Buscar por DNI
    Optional<Empleado> findByDni(String dni);

    // Buscar por email
    Optional<Empleado> findByEmail(String email);

    // Verificar si existe por DNI (excluyendo un ID específico para updates)
    boolean existsByDniAndIdEmpleadoNot(String dni, Integer idEmpleado);

    // Verificar si existe por email (excluyendo un ID específico para updates)
    boolean existsByEmailAndIdEmpleadoNot(String email, Integer idEmpleado);

    // Verificar existencia por DNI
    boolean existsByDni(String dni);

    // Verificar existencia por email
    boolean existsByEmail(String email);

    // Buscar empleados por texto en nombre, apellido, dni o email
    @Query("SELECT e FROM Empleado e WHERE " +
            "(LOWER(e.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(e.apellido) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(e.dni) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
            "AND e.activo = :activo")
    List<Empleado> findByTextoAndActivo(@Param("texto") String texto, @Param("activo") Boolean activo);

    // Contar empleados activos
    long countByActivoTrue();

    // Contar empleados por sector
    long countBySectorIdSectorAndActivoTrue(Integer sectorId);
}