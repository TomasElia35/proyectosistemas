package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoDTO {

    @NotNull(message = "El ID del insumo es obligatorio")
    private Integer idInsumo;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "ENTRADA|SALIDA|TRANSFERENCIA|DEVOLUCION",
            message = "El tipo debe ser ENTRADA, SALIDA, TRANSFERENCIA o DEVOLUCION")
    private String tipoMovimiento;

    private Integer sectorOrigen;
    private Integer sectorDestino;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 200, message = "El motivo no puede exceder 200 caracteres")
    private String motivo;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;

    @Size(max = 100, message = "El documento de referencia no puede exceder 100 caracteres")
    private String documentoReferencia;
}
