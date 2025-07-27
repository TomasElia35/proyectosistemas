package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class ApiResponseDTO {
    private boolean exito;
    private String mensaje;
    private Object datos;

    public ApiResponseDTO(boolean exito, String mensaje, Object datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public ApiResponseDTO(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }


}