package login.login.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionActivoDTO {
    private Long id;
    private Long idActivo;
    private String codigoInventarioActivo;
    private String nombreActivo;

    @NotNull(message = "El empleado es obligatorio")
    private Long idEmpleado;
    private String legajoEmpleado;
    private String nombreCompletoEmpleado;

    @NotNull(message = "El área es obligatoria")
    private Long idArea;
    private String nombreArea;

    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaDesasignacion;
    private String motivoDesasignacion;
    private String observaciones;
    private String usuarioAsignacion;
    private LocalDateTime fechaCreacion;

    // Campos calculados
    private Boolean esAsignacionActiva;
}
