package login.login.repository;

import login.login.entidad.TipoArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoArticuloRepository extends JpaRepository<TipoArticulo, Long> {

    Optional<TipoArticulo> findByNombre(String nombre);

    List<TipoArticulo> findByEsActivoTrue();

    @Query("SELECT t FROM TipoArticulo t WHERE t.categoria = :categoria AND t.esActivo = true")
    List<TipoArticulo> findByCategoria(@Param("categoria") TipoArticulo.CategoriaArticulo categoria);

    List<TipoArticulo> findByRequiereNumeroSerieTrueAndEsActivoTrue();

    List<TipoArticulo> findByRequiereMantenimientoTrueAndEsActivoTrue();

    @Query("SELECT t, COUNT(a) as cantidadActivos FROM TipoArticulo t LEFT JOIN ActivoTecnologico a ON t.id = a.tipoArticulo.id WHERE t.esActivo = true GROUP BY t.id")
    List<Object[]> findTiposWithActivosCount();
}