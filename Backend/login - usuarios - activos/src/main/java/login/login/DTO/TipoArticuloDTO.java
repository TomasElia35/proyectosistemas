package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import login.login.entidad.TipoArticulo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoArticuloDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "La categoría es obligatoria")
    private TipoArticulo.CategoriaArticulo categoria;

    private Boolean requiereNumeroSerie;
    private Integer vidaUtilDefectoAnios;
    private Boolean requiereMantenimiento;
    private Boolean esActivo;
}
