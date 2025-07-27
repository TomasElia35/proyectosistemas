package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoSimpleDTO {

    private Integer id;
    private String codigoActivo;
    private String tipoActivo; // Nombre de la tabla/entidad
    private String descripcion; // Marca + Modelo o descripción principal
    private String empleadoAsignado;
    private String sector;
    private String estado;
    private LocalDate fechaAsignacion;
    private String observaciones;
}