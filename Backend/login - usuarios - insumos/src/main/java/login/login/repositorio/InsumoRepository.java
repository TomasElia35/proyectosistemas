package login.login.repositorio;

import login.login.modelo.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Integer> {

    // Buscar por código interno único
    Optional<Insumo> findByCodigoInterno(String codigoInterno);

    // Verificar si existe por código interno
    boolean existsByCodigoInterno(String codigoInterno);

    // Buscar por número de serie único
    Optional<Insumo> findByNumeroSerie(String numeroSerie);

    // Verificar si existe por número de serie
    boolean existsByNumeroSerie(String numeroSerie);

    // Buscar por estado activo/inactivo
    List<Insumo> findByActivo(Boolean activo);

    // Buscar por nombre (ignorando mayúsculas/minúsculas)
    @Query("SELECT i FROM Insumo i WHERE LOWER(i.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND i.activo = true")
    List<Insumo> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    // Buscar por categoría
    @Query("SELECT i FROM Insumo i WHERE i.categoria.id = :categoriaId AND i.activo = true")
    List<Insumo> findByCategoriaId(@Param("categoriaId") Integer categoriaId);

    // Buscar por proveedor
    @Query("SELECT i FROM Insumo i WHERE i.proveedor.id = :proveedorId AND i.activo = true")
    List<Insumo> findByProveedorId(@Param("proveedorId") Integer proveedorId);

    // Buscar por sector
    @Query("SELECT i FROM Insumo i WHERE i.sector.id = :sectorId AND i.activo = true")
    List<Insumo> findBySectorId(@Param("sectorId") Integer sectorId);

    // Buscar por estado
    @Query("SELECT i FROM Insumo i WHERE i.estado.id = :estadoId AND i.activo = true")
    List<Insumo> findByEstadoId(@Param("estadoId") Integer estadoId);

    // Buscar insumos en garantía
    @Query("SELECT i FROM Insumo i WHERE i.fechaGarantia >= CURRENT_DATE AND i.activo = true")
    List<Insumo> findInsumosEnGarantia();

    // Buscar insumos fuera de garantía
    @Query("SELECT i FROM Insumo i WHERE i.fechaGarantia < CURRENT_DATE AND i.activo = true")
    List<Insumo> findInsumosFueraGarantia();

    // Buscar insumos por rango de fechas de compra
    @Query("SELECT i FROM Insumo i WHERE i.fechaCompra BETWEEN :fechaDesde AND :fechaHasta AND i.activo = true")
    List<Insumo> findByFechaCompraBetween(@Param("fechaDesde") LocalDate fechaDesde,
                                          @Param("fechaHasta") LocalDate fechaHasta);

    // Buscar insumos operativos
    @Query("SELECT i FROM Insumo i WHERE i.estado.nombre = 'OPERATIVO' AND i.activo = true")
    List<Insumo> findInsumosOperativos();

    // Buscar insumos en mantenimiento
    @Query("SELECT i FROM Insumo i WHERE i.estado.nombre = 'EN_MANTENIMIENTO' AND i.activo = true")
    List<Insumo> findInsumosEnMantenimiento();

    // Buscar insumos en reparación
    @Query("SELECT i FROM Insumo i WHERE i.estado.nombre = 'EN_REPARACION' AND i.activo = true")
    List<Insumo> findInsumosEnReparacion();

    // Obtener todas las categorías únicas
    @Query("SELECT DISTINCT c.nombre FROM Insumo i JOIN i.categoria c WHERE i.activo = true ORDER BY c.nombre")
    List<String> findDistinctCategorias();

    // Obtener todos los proveedores únicos
    @Query("SELECT DISTINCT p.nombreFantasia FROM Insumo i JOIN i.proveedor p WHERE i.activo = true ORDER BY p.nombreFantasia")
    List<String> findDistinctProveedores();

    // Obtener todos los sectores únicos
    @Query("SELECT DISTINCT s.nombre FROM Insumo i JOIN i.sector s WHERE i.activo = true ORDER BY s.nombre")
    List<String> findDistinctSectores();

    // Obtener todas las marcas únicas
    @Query("SELECT DISTINCT i.marca FROM Insumo i WHERE i.marca IS NOT NULL AND i.activo = true ORDER BY i.marca")
    List<String> findDistinctMarcas();

    // Buscar insumos activos ordenados por nombre
    @Query("SELECT i FROM Insumo i WHERE i.activo = true ORDER BY i.nombre ASC")
    List<Insumo> findAllActivosOrderByNombre();

    // Buscar por marca
    @Query("SELECT i FROM Insumo i WHERE LOWER(i.marca) LIKE LOWER(CONCAT('%', :marca, '%')) AND i.activo = true")
    List<Insumo> findByMarcaContainingIgnoreCase(@Param("marca") String marca);

    // Buscar por modelo
    @Query("SELECT i FROM Insumo i WHERE LOWER(i.modelo) LIKE LOWER(CONCAT('%', :modelo, '%')) AND i.activo = true")
    List<Insumo> findByModeloContainingIgnoreCase(@Param("modelo") String modelo);

    // Buscar por ubicación física
    @Query("SELECT i FROM Insumo i WHERE LOWER(i.ubicacionFisica) LIKE LOWER(CONCAT('%', :ubicacion, '%')) AND i.activo = true")
    List<Insumo> findByUbicacionFisicaContainingIgnoreCase(@Param("ubicacion") String ubicacion);

    // Contar insumos por categoría
    @Query("SELECT c.nombre, COUNT(i) FROM Insumo i JOIN i.categoria c WHERE i.activo = true GROUP BY c.nombre")
    List<Object[]> countInsumosByCategoria();

    // Contar insumos por estado
    @Query("SELECT e.nombre, COUNT(i) FROM Insumo i JOIN i.estado e WHERE i.activo = true GROUP BY e.nombre")
    List<Object[]> countInsumosByEstado();

    // Obtener insumos próximos a vencer garantía (30 días)
    @Query("SELECT i FROM Insumo i WHERE i.fechaGarantia BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, 30) AND i.activo = true")
    List<Insumo> findInsumosProximosVencerGarantia();
}