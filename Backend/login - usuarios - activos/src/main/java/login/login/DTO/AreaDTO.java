package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;
    private String centroCosto;
    private Long idResponsable;
    private String nombreResponsable;
    private String ubicacionFisica;
    private Boolean esActiva;
    private Integer cantidadEmpleados;
    private Integer cantidadActivos;
}
