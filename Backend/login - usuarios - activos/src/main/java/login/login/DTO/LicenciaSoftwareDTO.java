package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenciaSoftwareDTO {

    private Integer idLicencia;
    private String nombreSoftware;
    private String tipoSoftware;
    private String version;
    private String claveProducto;
    private String claveLicencia;
    private String emailAsociado;
    private String tipoLicencia;
    private Integer empleadoId;
    private String empleadoNombre;
    private Integer sectorId;
    private String sectorNombre;
    private LocalDate fechaCompra;
    private LocalDate fechaVencimiento;
    private Boolean activo;
    private LocalDate fechaInstalacion;
    private BigDecimal costo;
    private String proveedor;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private Integer creadoPorId;
    private String creadoPorNombre;
    private LocalDateTime fechaActualizacion;
    private Integer actualizadoPorId;
    private String actualizadoPorNombre;
    private Long diasParaVencimiento;
}