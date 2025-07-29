package login.login.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ActivosTecnologicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoTecnologico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigoInventario", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El código de inventario es obligatorio")
    private String codigoInventario;

    @Column(name = "numeroSerie", unique = true, length = 100)
    private String numeroSerie;

    @Column(name = "nombre", nullable = false, length = 200)
    @NotBlank(message = "El nombre del activo es obligatorio")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "modelo", length = 100)
    private String modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTipoArticulo", nullable = false)
    @NotNull(message = "El tipo de artículo es obligatorio")
    private TipoArticulo tipoArticulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProveedor", nullable = false)
    @NotNull(message = "El proveedor es obligatorio")
    private Proveedor proveedor;

    @Column(name = "costo", precision = 15, scale = 2)
    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    private BigDecimal costo;

    @Column(name = "fechaAdquisicion", nullable = false)
    @NotNull(message = "La fecha de adquisición es obligatoria")
    private LocalDate fechaAdquisicion;

    @Column(name = "garantiaMeses")
    private Integer garantiaMeses = 0;

    @Column(name = "fechaVencimientoGarantia")
    private LocalDate fechaVencimientoGarantia;

    @Column(name = "vidaUtilAnios")
    private Integer vidaUtilAnios = 3;

    @Column(name = "valorLibros", precision = 15, scale = 2)
    private BigDecimal valorLibros;

    @Column(name = "ubicacionFisica", length = 200)
    private String ubicacionFisica;

    @Column(name = "especificacionesTecnicas", columnDefinition = "JSON")
    private String especificacionesTecnicas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado", nullable = false)
    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArea", nullable = false)
    @NotNull(message = "El área es obligatoria")
    private Area area;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fechaUltimaModificacion", nullable = false)
    private LocalDateTime fechaUltimaModificacion;

    @Column(name = "usuarioCreacion", nullable = false, length = 100)
    @NotBlank(message = "El usuario de creación es obligatorio")
    private String usuarioCreacion;

    @Column(name = "usuarioModificacion", length = 100)
    private String usuarioModificacion;

    @Column(name = "esActivo", nullable = false)
    private Boolean esActivo = true;

    // Relaciones inversas
    @OneToMany(mappedBy = "activo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AsignacionActivo> asignaciones;

    @OneToMany(mappedBy = "activo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientoActivo> movimientos;

    @OneToMany(mappedBy = "activo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MantenimientoActivo> mantenimientos;

    // Métodos de conveniencia
    public boolean tieneGarantiaVigente() {
        return fechaVencimientoGarantia != null &&
                fechaVencimientoGarantia.isAfter(LocalDate.now());
    }

    public String getIdentificacion() {
        return codigoInventario + " - " + nombre;
    }
}