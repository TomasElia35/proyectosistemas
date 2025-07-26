package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoResponseDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String codigo;
    private BigDecimal precio;
    private Integer stock;
    private Integer stockMinimo;
    private String unidadMedida;
    private String categoria;
    private String proveedor;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;
    private Boolean estado;
    private boolean stockBajo;
    private UsuarioSimpleDTO usuarioCreador;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsuarioSimpleDTO {
        private Integer id;
        private String nombre;
        private String apellido;
        private String mail;
    }
}