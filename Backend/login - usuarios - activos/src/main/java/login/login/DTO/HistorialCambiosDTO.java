package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialCambiosDTO {

    private Integer idHistorial;
    private String tablaAfectada;
    private Integer idRegistro;
    private String tipoOperacion;
    private String datosAnteriores;
    private String datosNuevos;
    private Integer usuarioId;
    private String usuarioNombre;
    private LocalDateTime fechaCambio;
    private String observaciones;
}