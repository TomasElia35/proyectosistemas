package login.login.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import login.login.entidad.MantenimientoActivo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MantenimientoActivoDTO {
    private Long id;
    private Long idActivo;
    private String codigoInventarioActivo;
    private String nombreActivo;
    private MantenimientoActivo.TipoMantenimiento tipoMantenimiento;
    private LocalDate fechaProgramada;
    private LocalDate fechaRealizada;

    private Long idProveedor;
    private String nombreProveedor;
    private String tecnicoAsignado;

    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    @Digits(integer = 8, fraction = 2, message = "El formato del costo no es válido")
    private BigDecimal costo;

    private String descripcionProblema;
    private String descripcionSolucion;
    private String repuestosUtilizados;
    private Integer horasFueraServicio;

    private Long idEstadoResultante;
    private String nombreEstadoResultante;

    private String observaciones;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;

    // Campos calculados
    private Boolean estaCompletado;
    private Boolean estaAtrasado;
}

