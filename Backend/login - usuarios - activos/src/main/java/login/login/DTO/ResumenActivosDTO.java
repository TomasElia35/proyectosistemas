package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenActivosDTO {
    private Long totalActivos;
    private Long activosDisponibles;
    private Long activosAsignados;
    private Long activosEnMantenimiento;
    private Long activosFueraServicio;
    private Long activosConGarantiaVigente;
    private BigDecimal valorTotalActivos;
    private BigDecimal valorTotalEnLibros;

    // Por categoría
    private Long activosHardware;
    private Long activosSoftware;
    private Long activosPeriferico;
    private Long activosAccesorio;

    // Mantenimientos
    private Long mantenimientosPendientes;
    private Long mantenimientosAtrasados;
    private Long mantenimientosCompletadosUltimoMes;
}