package login.login.DTO;

import login.login.entidad.MovimientoActivo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoActivoDTO {
    private Long id;
    private Long idActivo;
    private String codigoInventarioActivo;
    private String nombreActivo;
    private MovimientoActivo.TipoMovimiento tipoMovimiento;
    private LocalDateTime fechaMovimiento;

    // Empleados
    private Long idEmpleadoOrigen;
    private String nombreEmpleadoOrigen;
    private Long idEmpleadoDestino;
    private String nombreEmpleadoDestino;

    // Áreas
    private Long idAreaOrigen;
    private String nombreAreaOrigen;
    private Long idAreaDestino;
    private String nombreAreaDestino;

    // Estados
    private Long idEstadoAnterior;
    private String nombreEstadoAnterior;
    private Long idEstadoNuevo;
    private String nombreEstadoNuevo;

    private String motivoMovimiento;
    private String observaciones;
    private String usuarioMovimiento;
    private LocalDateTime fechaCreacion;
}
