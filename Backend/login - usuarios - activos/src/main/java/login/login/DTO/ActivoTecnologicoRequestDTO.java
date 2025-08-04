package login.login.DTO;

import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivoTecnologicoRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String codigoInventario;

    @Size(max = 100)
    private String codigoTecnico;

    @NotBlank
    @Size(max = 200)
    private String nombre;

    private String descripcion;

    @NotNull
    private Long idModeloArticulo;

    @NotNull
    private Long idProveedor;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal costo;

    @NotNull
    private LocalDate fechaAdquisicion;

    @NotNull
    @Min(0)
    private Integer garantiaMeses;

    private LocalDate fechaVencimientoGarantia;

    @NotNull
    private Long idEstado;

    @NotNull
    private Long idEmpleado;

    private String observaciones;

    @NotBlank
    private String usuarioCreacion;

    private String usuarioModificacion;

    private Boolean activo = true;
}
