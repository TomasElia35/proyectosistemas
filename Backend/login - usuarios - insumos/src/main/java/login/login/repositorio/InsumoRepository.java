package login.login.repositorio;

import login.login.modelo.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Integer> {

    // Buscar por código único
    Optional<Insumo> findByCodigo(String codigo);

    // Verificar si existe por código
    boolean existsByCodigo(String codigo);

    // Buscar por estado (activo/inactivo)
    List<Insumo> findByEstado(Boolean estado);

    // Buscar por nombre (ignorando mayúsculas/minúsculas)
    @Query("SELECT i FROM Insumo i WHERE LOWER(i.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Insumo> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    // Buscar por categoría
    List<Insumo> findByCategoria(String categoria);

    // Buscar por proveedor
    List<Insumo> findByProveedor(String proveedor);

    // Buscar insumos con stock bajo (stock <= stockMinimo)
    @Query("SELECT i FROM Insumo i WHERE i.stock <= i.stockMinimo AND i.estado = true")
    List<Insumo> findInsumosConStockBajo();

    // Buscar insumos por rango de stock
    @Query("SELECT i FROM Insumo i WHERE i.stock BETWEEN :minStock AND :maxStock AND i.estado = true")
    List<Insumo> findByStockBetween(@Param("minStock") Integer minStock, @Param("maxStock") Integer maxStock);

    // Obtener todas las categorías únicas
    @Query("SELECT DISTINCT i.categoria FROM Insumo i WHERE i.categoria IS NOT NULL AND i.estado = true ORDER BY i.categoria")
    List<String> findDistinctCategorias();

    // Obtener todos los proveedores únicos
    @Query("SELECT DISTINCT i.proveedor FROM Insumo i WHERE i.proveedor IS NOT NULL AND i.estado = true ORDER BY i.proveedor")
    List<String> findDistinctProveedores();

    // Buscar insumos activos ordenados por nombre
    @Query("SELECT i FROM Insumo i WHERE i.estado = true ORDER BY i.nombre ASC")
    List<Insumo> findAllActivosOrderByNombre();
}