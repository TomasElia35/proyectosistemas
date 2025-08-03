package login.login.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletarMantenimientoDTO {
    @NotNull(message = "La fecha de realización es obligatoria")
    @PastOrPresent(message = "La fecha de realización no puede ser futura")
    private LocalDate fechaRealizada;

    @NotBlank(message = "La descripción de la solución es obligatoria")
    private String descripcionSolucion;

    private String repuestosUtilizados;

    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    @Digits(integer = 8, fraction = 2, message = "El formato del costo no es válido")
    private BigDecimal costo;

    @Min(value = 0, message = "Las horas fuera de servicio deben ser mayor o igual a 0")
    private Integer horasFueraServicio;

    @NotNull(message = "El estado resultante es obligatorio")
    private Long idEstadoResultante;

    private String observaciones;
}
