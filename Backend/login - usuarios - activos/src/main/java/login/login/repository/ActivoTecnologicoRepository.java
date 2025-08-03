package login.login.repository;

import login.login.entidad.ActivoTecnologico;
import login.login.entidad.TipoArticulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivoTecnologicoRepository extends JpaRepository<ActivoTecnologico, Long> {

    Optional<ActivoTecnologico> findByCodigoInventario(String codigoInventario);

    Optional<ActivoTecnologico> findByNumeroSerie(String numeroSerie);

    List<ActivoTecnologico> findByEsActivoTrue();

    List<ActivoTecnologico> findByAreaIdAndEsActivoTrue(Long areaId);

    List<ActivoTecnologico> findByEstadoIdAndEsActivoTrue(Long estadoId);

    List<ActivoTecnologico> findByTipoArticuloIdAndEsActivoTrue(Long tipoArticuloId);

    List<ActivoTecnologico> findByProveedorIdAndEsActivoTrue(Long proveedorId);

    @Query("SELECT a FROM ActivoTecnologico a WHERE a.fechaVencimientoGarantia IS NOT NULL AND a.fechaVencimientoGarantia > CURRENT_DATE AND a.esActivo = true")
    List<ActivoTecnologico> findActivosConGarantiaVigente();

    @Query("SELECT a FROM ActivoTecnologico a LEFT JOIN AsignacionActivo aa ON a.id = aa.activo.id AND aa.fechaDesasignacion IS NULL WHERE aa.id IS NULL AND a.esActivo = true")
    List<ActivoTecnologico> findActivosDisponibles();

    @Query("SELECT a FROM ActivoTecnologico a INNER JOIN AsignacionActivo aa ON a.id = aa.activo.id WHERE aa.fechaDesasignacion IS NULL AND a.esActivo = true")
    List<ActivoTecnologico> findActivosAsignados();

    @Query(value = "SELECT * FROM ActivosTecnologicos a WHERE " +
            "(:codigoInventario IS NULL OR a.codigoInventario LIKE %:codigoInventario%) AND " +
            "(:numeroSerie IS NULL OR a.numeroSerie LIKE %:numeroSerie%) AND " +
            "(:nombre IS NULL OR a.nombre LIKE %:nombre%) AND " +
            "(:marca IS NULL OR a.marca LIKE %:marca%) AND " +
            "(:modelo IS NULL OR a.modelo LIKE %:modelo%) AND " +
            "(:idTipoArticulo IS NULL OR a.idTipoArticulo = :idTipoArticulo) AND " +
            "(:idProveedor IS NULL OR a.idProveedor = :idProveedor) AND " +
            "(:idEstado IS NULL OR a.idEstado = :idEstado) AND " +
            "(:idArea IS NULL OR a.idArea = :idArea) AND " +
            "(:fechaDesde IS NULL OR a.fechaAdquisicion >= :fechaDesde) AND " +
            "(:fechaHasta IS NULL OR a.fechaAdquisicion <= :fechaHasta) AND " +
            "(:costoMinimo IS NULL OR a.costo >= :costoMinimo) AND " +
            "(:costoMaximo IS NULL OR a.costo <= :costoMaximo) AND " +
            "(:esActivo IS NULL OR a.esActivo = :esActivo)",
            nativeQuery = true)
    Page<ActivoTecnologico> findByFiltros(@Param("codigoInventario") String codigoInventario,
                                          @Param("numeroSerie") String numeroSerie,
                                          @Param("nombre") String nombre,
                                          @Param("marca") String marca,
                                          @Param("modelo") String modelo,
                                          @Param("idTipoArticulo") Long idTipoArticulo,
                                          @Param("idProveedor") Long idProveedor,
                                          @Param("idEstado") Long idEstado,
                                          @Param("idArea") Long idArea,
                                          @Param("fechaDesde") LocalDate fechaDesde,
                                          @Param("fechaHasta") LocalDate fechaHasta,
                                          @Param("costoMinimo") BigDecimal costoMinimo,
                                          @Param("costoMaximo") BigDecimal costoMaximo,
                                          @Param("esActivo") Boolean esActivo,
                                          Pageable pageable);

    @Query("SELECT COUNT(a) FROM ActivoTecnologico a WHERE a.esActivo = true")
    Long countTotalActivos();

    @Query("SELECT COUNT(a) FROM ActivoTecnologico a WHERE a.tipoArticulo.categoria = :categoria AND a.esActivo = true")
    Long countByCategoria(@Param("categoria") TipoArticulo.CategoriaArticulo categoria);

    @Query("SELECT SUM(a.costo) FROM ActivoTecnologico a WHERE a.esActivo = true")
    BigDecimal sumTotalCosto();

    @Query("SELECT SUM(a.valorLibros) FROM ActivoTecnologico a WHERE a.esActivo = true")
    BigDecimal sumTotalValorLibros();
}