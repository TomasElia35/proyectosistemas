package login.login.repositorio;

import login.login.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByMail(String mail);
    boolean existsByMail(String mail);
    List<Usuario> findByEstado(Boolean estado);
    List<Usuario> findByRolId(Integer rolId);
    @Query("SELECT u FROM Usuario u WHERE " +
            "LOWER(CONCAT(u.nombre, ' ', u.apellido)) LIKE LOWER(CONCAT('%', :nombreCompleto, '%')) " +
            "AND u.activo = true")
    List<Usuario> findByNombreCompletoContaining(@Param("nombreCompleto") String nombreCompleto);

    // Buscar por rol
    List<Usuario> findByRolIdRolAndActivoTrue(Integer rolId);

    // Verificar si existe por email (excluyendo un ID específico)
    boolean existsByEmailAndIdUsuarioNot(String email, Integer idUsuario);

    // Buscar usuarios activos ordenados por nombre
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombre, u.apellido")
    List<Usuario> findUsuariosActivosOrdenados();

    // Contar usuarios por rol
    long countByRolIdRolAndActivoTrue(Integer rolId);
}