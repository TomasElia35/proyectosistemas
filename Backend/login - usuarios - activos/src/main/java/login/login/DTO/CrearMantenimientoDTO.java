package login.login.DTO;

import jakarta.validation.constraints.*;
import login.login.entidad.MantenimientoActivo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearMantenimientoDTO {
    @NotNull(message = "El activo es obligatorio")
    private Long idActivo;

    @NotNull(message = "El tipo de mantenimiento es obligatorio")
    private MantenimientoActivo.TipoMantenimiento tipoMantenimiento;

    @NotNull(message = "La fecha programada es obligatoria")
    @Future(message = "La fecha programada debe ser futura")
    private LocalDate fechaProgramada;

    private Long idProveedor;

    @Size(max = 150, message = "El nombre del técnico no puede exceder 150 caracteres")
    private String tecnicoAsignado;

    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    @Digits(integer = 8, fraction = 2, message = "El formato del costo no es válido")
    private BigDecimal costo = BigDecimal.ZERO;

    private String descripcionProblema;

    @Min(value = 0, message = "Las horas fuera de servicio deben ser mayor o igual a 0")
    private Integer horasFueraServicio = 0;

    @NotNull(message = "El estado resultante es obligatorio")
    private Long idEstadoResultante;

    private String observaciones;

    @NotBlank(message = "El usuario de creación es obligatorio")
    private String usuarioCreacion;
}
