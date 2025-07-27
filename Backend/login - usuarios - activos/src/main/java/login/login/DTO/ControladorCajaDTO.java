package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControladorCajaDTO {

    private Integer idControlador;
    private String codigoControlador;
    private String direccionIp;
    private String usuario;
    private String contrasena;
    private String ubicacionFisica;
    private Integer sectorId;
    private String sectorNombre;
    private Boolean activo;
    private LocalDate fechaConfiguracion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private Integer creadoPorId;
    private String creadoPorNombre;
    private LocalDateTime fechaActualizacion;
    private Integer actualizadoPorId;
    private String actualizadoPorNombre;
}
