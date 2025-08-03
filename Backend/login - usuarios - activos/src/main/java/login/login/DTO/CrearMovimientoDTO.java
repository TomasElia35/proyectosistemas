package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import login.login.entidad.MovimientoActivo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearMovimientoDTO {
    @NotNull(message = "El activo es obligatorio")
    private Long idActivo;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private MovimientoActivo.TipoMovimiento tipoMovimiento;

    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    private Long idEmpleadoOrigen;
    private Long idEmpleadoDestino;
    private Long idAreaOrigen;
    private Long idAreaDestino;

    @NotNull(message = "El estado anterior es obligatorio")
    private Long idEstadoAnterior;

    @NotNull(message = "El estado nuevo es obligatorio")
    private Long idEstadoNuevo;

    @NotBlank(message = "El motivo del movimiento es obligatorio")
    private String motivoMovimiento;

    private String observaciones;

    @NotBlank(message = "El usuario del movimiento es obligatorio")
    private String usuarioMovimiento;
}
