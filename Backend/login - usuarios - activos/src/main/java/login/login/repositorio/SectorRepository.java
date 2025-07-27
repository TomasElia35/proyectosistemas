package login.login.repositorio;

import login.login.entidad.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Integer> {

    // Buscar sectores activos
    List<Sector> findByActivoTrue();

    // Buscar por nombre (case insensitive)
    Optional<Sector> findByNombreIgnoreCase(String nombre);

    // Verificar si existe por nombre (excluyendo un ID específico para updates)
    boolean existsByNombreIgnoreCaseAndIdSectorNot(String nombre, Integer idSector);

    // Verificar si existe por nombre
    boolean existsByNombreIgnoreCase(String nombre);

    // Buscar sectores por texto en nombre o descripción
    @Query("SELECT s FROM Sector s WHERE " +
            "(LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
            "AND s.activo = :activo")
    List<Sector> findByTextoAndActivo(@Param("texto") String texto, @Param("activo") Boolean activo);

    // Contar sectores activos
    long countByActivoTrue();
}