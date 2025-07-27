package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {

    private Integer idEmpleado;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private Integer sectorId;
    private String sectorNombre;
    private Boolean activo;
    private LocalDate fechaIngreso;
    private LocalDateTime fechaCreacion;
    private Integer creadoPorId;
    private String creadoPorNombre;
    private String nombreCompleto;
}