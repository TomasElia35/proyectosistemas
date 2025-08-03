package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import login.login.entidad.Empleado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {
    private Long id;

    @NotBlank(message = "El legajo es obligatorio")
    private String legajo;

    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    private LocalDate fechaNacimiento;
    private String emailCorporativo;
    private String celularCorporativo;

    @NotNull(message = "El área es obligatoria")
    private Long idArea;
    private String nombreArea;

    private LocalDate fechaIngreso;
    private LocalDate fechaBaja;
    private Empleado.TipoContrato tipoContrato;
    private Boolean esActivo;
    private Boolean esResponsableArea;
    private String observaciones;

    // Campos calculados
    private String nombreCompleto;
    private Integer cantidadActivosAsignados;
}
