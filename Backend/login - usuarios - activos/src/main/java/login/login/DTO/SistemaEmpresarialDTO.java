package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SistemaEmpresarialDTO {

    private Integer idSistema;
    private String nombreSistema;
    private String tipoSistema;
    private String tipoSistemaDisplayName;
    private String urlAcceso;
    private String usuario;
    private String contrasena;
    private Integer empleadoId;
    private String empleadoNombre;
    private Integer sectorId;
    private String sectorNombre;
    private String rolPermisos;
    private Boolean activo;
    private LocalDate fechaAsignacion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private Integer creadoPorId;
    private String creadoPorNombre;
    private LocalDateTime fechaActualizacion;
    private Integer actualizadoPorId;
    private String actualizadoPorNombre;
}