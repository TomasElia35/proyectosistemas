package login.login.repositorio;

import login.login.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByMail(String mail);
    boolean existsByMail(String mail);
    List<Usuario> findByEstado(Boolean estado);
    List<Usuario> findByRolId(Integer rolId);
}