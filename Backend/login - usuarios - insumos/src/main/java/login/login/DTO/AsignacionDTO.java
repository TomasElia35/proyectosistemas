package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionDTO {

    @NotNull(message = "El ID del insumo es obligatorio")
    private Integer idInsumo;

    @NotBlank(message = "El tipo de asignación es obligatorio")
    @Pattern(regexp = "EMPLEADO|SECTOR|PROYECTO",
            message = "El tipo debe ser EMPLEADO, SECTOR o PROYECTO")
    private String tipoAsignacion;

    @Size(max = 100, message = "El empleado asignado no puede exceder 100 caracteres")
    private String empleadoAsignado;

    @Size(max = 20, message = "El legajo no puede exceder 20 caracteres")
    private String legajoEmpleado;

    private Integer sectorAsignado;

    @Size(max = 100, message = "El proyecto no puede exceder 100 caracteres")
    private String proyectoAsignado;

    private LocalDate fechaDevolucionPrevista;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;

    @Size(max = 500, message = "La condición de entrega no puede exceder 500 caracteres")
    private String condicionEntrega;
}