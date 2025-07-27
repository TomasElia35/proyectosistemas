// ===== InsumoRequestDTO.java =====
package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "El código interno es obligatorio")
    @Size(max = 50, message = "El código interno no puede exceder 50 caracteres")
    private String codigoInterno;

    @Size(max = 100, message = "El código de barras no puede exceder 100 caracteres")
    private String codigoBarras;

    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    private String marca;

    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String modelo;

    @Size(max = 100, message = "El número de serie no puede exceder 100 caracteres")
    private String numeroSerie;

    private LocalDate fechaCompra;

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio de compra no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal precioCompra;

    private LocalDate fechaGarantia;

    @Min(value = 0, message = "Los meses de garantía no pueden ser negativos")
    private Integer mesesGarantia;

    @Size(max = 1000, message = "Las especificaciones técnicas no pueden exceder 1000 caracteres")
    private String especificacionesTecnicas;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;

    @Size(max = 200, message = "La ubicación física no puede exceder 200 caracteres")
    private String ubicacionFisica;

    // IDs de entidades relacionadas
    private Integer idProveedor;

    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;

    private Integer idSector;

    @NotNull(message = "El estado es obligatorio")
    private Integer idEstado;

    private Boolean activo = true;
}