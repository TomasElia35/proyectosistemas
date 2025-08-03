package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;
    private Boolean permiteAsignacion;
    private Boolean requiereAprobacion;
    private Boolean esEstadoFinal;
    private Boolean esActivo;
}
