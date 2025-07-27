package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerifericoDTO {

    private Integer idPeriferico;
    private String codigoActivo;
    private String tipo;
    private String marca;
    private String modelo;
    private Integer empleadoId;
    private String empleadoNombre;
    private Integer sectorId;
    private String sectorNombre;
    private Integer estadoId;
    private String estadoNombre;
    private LocalDate fechaAsignacion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private Integer creadoPorId;
    private String creadoPorNombre;
    private LocalDateTime fechaActualizacion;
    private Integer actualizadoPorId;
    private String actualizadoPorNombre;
}