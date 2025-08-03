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
public class ActualizarActivoTecnologicoDTO {
    @Size(max = 100, message = "El número de serie no puede exceder 100 caracteres")
    private String numeroSerie;

    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    private String descripcion;

    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    private String marca;

    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String modelo;

    private Long idTipoArticulo;

    private Long idProveedor;

    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El formato del costo no es válido")
    private BigDecimal costo;

    private LocalDate fechaAdquisicion;

    @Min(value = 0, message = "Los meses de garantía deben ser mayor o igual a 0")
    private Integer garantiaMeses;

    @Min(value = 1, message = "La vida útil debe ser mayor a 0")
    private Integer vidaUtilAnios;

    @DecimalMin(value = "0.0", message = "El valor en libros debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El formato del valor en libros no es válido")
    private BigDecimal valorLibros;

    @Size(max = 200, message = "La ubicación física no puede exceder 200 caracteres")
    private String ubicacionFisica;

    private String especificacionesTecnicas;

    private Long idArea;

    private String observaciones;

    @NotBlank(message = "El usuario de modificación es obligatorio")
    private String usuarioModificacion;
}
