package login.login.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "AsignacionesActivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idActivo", nullable = false)
    @NotNull(message = "El activo es obligatorio")
    private ActivoTecnologico activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleado", nullable = false)
    @NotNull(message = "El empleado es obligatorio")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArea", nullable = false)
    @NotNull(message = "El área es obligatoria")
    private Area area;

    @Column(name = "fechaAsignacion", nullable = false)
    @NotNull(message = "La fecha de asignación es obligatoria")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fechaDesasignacion")
    private LocalDateTime fechaDesasignacion; // NULL = asignación activa

    @Column(name = "motivoDesasignacion", length = 500)
    private String motivoDesasignacion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "usuarioAsignacion", nullable = false, length = 100)
    private String usuarioAsignacion;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Método de conveniencia
    public boolean esAsignacionActiva() {
        return fechaDesasignacion == null;
    }
}
