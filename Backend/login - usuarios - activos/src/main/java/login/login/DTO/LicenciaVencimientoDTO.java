package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenciaVencimientoDTO {

    private Integer idLicencia;
    private String nombreSoftware;
    private String tipoSoftware;
    private LocalDate fechaVencimiento;
    private Long diasRestantes;
    private String empleadoAsignado;
    private String sector;
    private Boolean activo;
}