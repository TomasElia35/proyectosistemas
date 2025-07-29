package login.login.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "MantenimientosActivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MantenimientoActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idActivo", nullable = false)
    @NotNull(message = "El activo es obligatorio")
    private ActivoTecnologico activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoMantenimiento", nullable = false)
    @NotNull(message = "El tipo de mantenimiento es obligatorio")
    private TipoMantenimiento tipoMantenimiento;

    @Column(name = "fechaProgramada", nullable = false)
    @NotNull(message = "La fecha programada es obligatoria")
    private LocalDate fechaProgramada;

    @Column(name = "fechaRealizada")
    private LocalDate fechaRealizada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    @Column(name = "tecnicoAsignado", length = 150)
    private String tecnicoAsignado;

    @Column(name = "costo", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    private BigDecimal costo = BigDecimal.ZERO;

    @Column(name = "descripcionProblema", columnDefinition = "TEXT")
    private String descripcionProblema;

    @Column(name = "descripcionSolucion", columnDefinition = "TEXT")
    private String descripcionSolucion;

    @Column(name = "repuestosUtilizados", columnDefinition = "TEXT")
    private String repuestosUtilizados;

    @Column(name = "horasFueraServicio")
    private Integer horasFueraServicio = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstadoResultante", nullable = false)
    @NotNull(message = "El estado resultante es obligatorio")
    private Estado estadoResultante;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "usuarioCreacion", nullable = false, length = 100)
    @NotBlank(message = "El usuario de creación es obligatorio")
    private String usuarioCreacion;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public enum TipoMantenimiento {
        PREVENTIVO, CORRECTIVO, GARANTIA, UPGRADE
    }

    // Métodos de conveniencia
    public boolean estaCompletado() {
        return fechaRealizada != null;
    }

    public boolean estaAtrasado() {
        return fechaRealizada == null && fechaProgramada.isBefore(LocalDate.now());
    }
}