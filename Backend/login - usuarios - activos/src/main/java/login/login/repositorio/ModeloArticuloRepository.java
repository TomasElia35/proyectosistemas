package login.login.repositorio;

import login.login.modelo.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloArticuloRepository extends JpaRepository<ModeloArticulo, Long> {
}
