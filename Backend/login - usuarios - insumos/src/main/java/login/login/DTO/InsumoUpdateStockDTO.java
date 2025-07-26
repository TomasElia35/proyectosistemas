package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoUpdateStockDTO {

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotBlank(message = "El tipo de operación es obligatorio")
    @Pattern(regexp = "ENTRADA|SALIDA", message = "El tipo debe ser ENTRADA o SALIDA")
    private String tipoOperacion; // ENTRADA o SALIDA

    @Size(max = 200, message = "La observación no puede exceder 200 caracteres")
    private String observacion;
}