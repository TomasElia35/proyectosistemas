package login.login.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ActivosTecnologicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivoTecnologico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigoInventario;

    @Column(unique = true, length = 100)
    private String codigoTecnico;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Lob
    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idModeloArticulo")
    private ModeloArticulo modeloArticulo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal costo;

    @Column(nullable = false)
    private LocalDate fechaAdquisicion;

    @Column(nullable = false)
    private Integer garantiaMeses;

    private LocalDate fechaVencimientoGarantia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idEstado")
    private Estado estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idEmpleado")
    private Empleado empleado;

    @Lob
    private String observaciones;

    @Column(nullable = false)
    private String usuarioCreacion;

    private String usuarioModificacion;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @Column(nullable = false)
    private LocalDate fechaUltimaModificacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDate.now();
        fechaUltimaModificacion = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaUltimaModificacion = LocalDate.now();
    }
}
