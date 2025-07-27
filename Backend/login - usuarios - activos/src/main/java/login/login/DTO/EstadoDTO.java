package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {

    private Integer idEstado;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}