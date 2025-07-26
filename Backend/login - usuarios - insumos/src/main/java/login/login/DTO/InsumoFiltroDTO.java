package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoFiltroDTO {

    private String nombre;
    private String categoria;
    private String proveedor;
    private Boolean estado;
    private Boolean soloStockBajo;
    private Integer stockMinimo;
    private Integer stockMaximo;
}