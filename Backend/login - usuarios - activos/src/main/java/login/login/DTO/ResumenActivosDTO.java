package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenActivosDTO {

    private Integer idEmpleado;
    private String nombreCompleto;
    private String sector;
    private Long dispositivosMoviles;
    private Long computadoras;
    private Long scanners;
    private Long telefonos;
    private Long monitores;
    private Long perifericos;
    private Long dispositivosAlmacenamiento;
    private Long correosElectronicos;
    private Long sistemasEmpresariales;
    private Long licenciasSoftware;
    private Long totalActivos;
}