package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDashboardDTO {

    // Contadores generales
    private Long totalEmpleados;
    private Long totalSectores;
    private Long totalActivos;

    // Equipos y Hardware
    private Long totalDispositivosMoviles;
    private Long totalComputadoras;
    private Long totalImpresoras;
    private Long totalScanners;
    private Long totalTelefonos;
    private Long totalMonitores;
    private Long totalEquiposRed;
    private Long totalPerifericos;
    private Long totalEquiposAudiovisuales;
    private Long totalDispositivosAlmacenamiento;
    private Long totalEquiposEnergia;
    private Long totalCamarasSeguridad;

    // Credenciales y Accesos
    private Long totalCorreosElectronicos;
    private Long totalRedesWifi;
    private Long totalControladoresCajas;
    private Long totalPlataformasServicios;
    private Long totalSistemasEmpresariales;
    private Long totalServidoresFtp;

    // Licencias
    private Long totalLicenciasSoftware;
    private Long licenciasActivasCount;
    private Long licenciasVencidasCount;
    private Long licenciasPorVencerCount; // Próximas 30 días

    // Estados
    private Long activosEnUso;
    private Long activosInactivos;
    private Long activosEnReparacion;
    private Long activosDadosDeBaja;
}