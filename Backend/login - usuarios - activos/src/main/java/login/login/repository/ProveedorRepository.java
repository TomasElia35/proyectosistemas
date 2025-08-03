package login.login.repository;

import login.login.entidad.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByNombre(String nombre);

    Optional<Proveedor> findByCuit(String cuit);

    List<Proveedor> findByEsActivoTrue();

    @Query("SELECT p FROM Proveedor p WHERE p.formaPago = :formaPago AND p.esActivo = true")
    List<Proveedor> findByFormaPago(@Param("formaPago") Proveedor.FormaPago formaPago);

    @Query("SELECT p FROM Proveedor p WHERE p.monedaOperacion = :moneda AND p.esActivo = true")
    List<Proveedor> findByMonedaOperacion(@Param("moneda") Proveedor.MonedaOperacion moneda);

    @Query("SELECT p FROM Proveedor p WHERE (p.nombre LIKE %:busqueda% OR p.cuit LIKE %:busqueda%) AND p.esActivo = true")
    List<Proveedor> buscarProveedores(@Param("busqueda") String busqueda);

    @Query("SELECT p, COUNT(a) as cantidadActivos FROM Proveedor p LEFT JOIN ActivoTecnologico a ON p.id = a.proveedor.id WHERE p.esActivo = true GROUP BY p.id")
    List<Object[]> findProveedoresWithActivosCount();

    @Query("SELECT p, COUNT(m) as cantidadMantenimientos FROM Proveedor p LEFT JOIN MantenimientoActivo m ON p.id = m.proveedor.id WHERE p.esActivo = true GROUP BY p.id")
    List<Object[]> findProveedoresWithMantenimientosCount();
}