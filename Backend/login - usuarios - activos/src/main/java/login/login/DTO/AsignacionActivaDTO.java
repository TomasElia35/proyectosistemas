package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionActivaDTO {
    private Long idAsignacion;
    private Long idEmpleado;
    private String legajoEmpleado;
    private String nombreCompletoEmpleado;
    private String emailEmpleado;
    private Long idArea;
    private String nombreArea;
    private LocalDateTime fechaAsignacion;
    private String usuarioAsignacion;
}
