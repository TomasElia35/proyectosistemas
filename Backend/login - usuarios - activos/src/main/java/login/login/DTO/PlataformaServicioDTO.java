package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlataformaServicioDTO {

    private Integer idPlataforma;
    private String nombrePlataforma;
    private String tipoServicio;
    private String tipoServicioDisplayName;
    private String usuario;
    private String contrasena;
    private String emailAsociado;
    private String urlAcceso;
    private String tipoCuenta;
    private Integer sectorId;
    private String sectorNombre;
    private Boolean activo;
    private LocalDate fechaSuscripcion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private Integer creadoPorId;
    private String creadoPorNombre;
    private LocalDateTime fechaActualizacion;
    private Integer actualizadoPorId;
    private String actualizadoPorNombre;
}
