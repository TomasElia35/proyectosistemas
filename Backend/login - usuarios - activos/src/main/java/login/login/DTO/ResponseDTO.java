package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private boolean exito;
    private String mensaje;
    private T datos;
    private String codigo;

    public static <T> ResponseDTO<T> exito(T datos) {
        return new ResponseDTO<>(true, "Operación exitosa", datos, "200");
    }

    public static <T> ResponseDTO<T> exito(String mensaje, T datos) {
        return new ResponseDTO<>(true, mensaje, datos, "200");
    }

    public static <T> ResponseDTO<T> error(String mensaje) {
        return new ResponseDTO<>(false, mensaje, null, "400");
    }

    public static <T> ResponseDTO<T> error(String mensaje, String codigo) {
        return new ResponseDTO<>(false, mensaje, null, codigo);
    }
}