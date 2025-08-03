package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesasignarActivoDTO {
    @NotBlank(message = "El motivo de desasignación es obligatorio")
    private String motivoDesasignacion;

    private String observaciones;

    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;
}
