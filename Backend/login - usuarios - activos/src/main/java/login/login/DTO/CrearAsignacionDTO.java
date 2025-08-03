package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearAsignacionDTO {
    @NotNull(message = "El activo es obligatorio")
    private Long idActivo;

    @NotNull(message = "El empleado es obligatorio")
    private Long idEmpleado;

    @NotNull(message = "El área es obligatoria")
    private Long idArea;

    private String observaciones;

    @NotBlank(message = "El usuario de asignación es obligatorio")
    private String usuarioAsignacion;
}
