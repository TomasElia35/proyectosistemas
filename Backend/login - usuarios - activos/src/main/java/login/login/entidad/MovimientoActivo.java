package login.login.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "MovimientosActivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idActivo", nullable = false)
    @NotNull(message = "El activo es obligatorio")
    private ActivoTecnologico activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoMovimiento", nullable = false)
    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    @Column(name = "fechaMovimiento", nullable = false)
    @NotNull(message = "La fecha de movimiento es obligatoria")
    private LocalDateTime fechaMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleadoOrigen")
    private Empleado empleadoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleadoDestino")
    private Empleado empleadoDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAreaOrigen")
    private Area areaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAreaDestino")
    private Area areaDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstadoAnterior", nullable = false)
    @NotNull(message = "El estado anterior es obligatorio")
    private Estado estadoAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstadoNuevo", nullable = false)
    @NotNull(message = "El estado nuevo es obligatorio")
    private Estado estadoNuevo;

    @Column(name = "motivoMovimiento", nullable = false, length = 500)
    @NotBlank(message = "El motivo del movimiento es obligatorio")
    private String motivoMovimiento;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "usuarioMovimiento", nullable = false, length = 100)
    @NotBlank(message = "El usuario del movimiento es obligatorio")
    private String usuarioMovimiento;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public enum TipoMovimiento {
        ASIGNACION, DEVOLUCION, TRANSFERENCIA, MANTENIMIENTO, BAJA
    }
}
