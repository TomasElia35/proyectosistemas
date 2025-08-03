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
public class CrearActivoTecnologicoDTO {
    @NotBlank(message = "El código de inventario es obligatorio")
    @Size(max = 50, message = "El código de inventario no puede exceder 50 caracteres")
    private String codigoInventario;

    @Size(max = 100, message = "El número de serie no puede exceder 100 caracteres")
    private String numeroSerie;

    @NotBlank(message = "El nombre del activo es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    private String descripcion;

    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    private String marca;

    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String modelo;

    @NotNull(message = "El tipo de artículo es obligatorio")
    private Long idTipoArticulo;

    @NotNull(message = "El proveedor es obligatorio")
    private Long idProveedor;

    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El formato del costo no es válido")
    private BigDecimal costo;

    @NotNull(message = "La fecha de adquisición es obligatoria")
    private LocalDate fechaAdquisicion;

    @Min(value = 0, message = "Los meses de garantía deben ser mayor o igual a 0")
    private Integer garantiaMeses = 0;

    @Min(value = 1, message = "La vida útil debe ser mayor a 0")
    private Integer vidaUtilAnios = 3;

    @DecimalMin(value = "0.0", message = "El valor en libros debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El formato del valor en libros no es válido")
    private BigDecimal valorLibros;

    @Size(max = 200, message = "La ubicación física no puede exceder 200 caracteres")
    private String ubicacionFisica;

    private String especificacionesTecnicas;

    @NotNull(message = "El área es obligatoria")
    private Long idArea;

    private String observaciones;

    @NotBlank(message = "El usuario de creación es obligatorio")
    private String usuarioCreacion;
}
