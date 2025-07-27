package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaFiltroDTO {

    private String textoBusqueda;
    private List<Integer> sectoresIds;
    private List<Integer> empleadosIds;
    private List<Integer> estadosIds;
    private String tipoActivo; // Para filtrar por tipo de activo específico
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Boolean activo;
    private Integer pagina;
    private Integer tamanoPagina;
    private String ordenarPor;
    private String direccionOrden; // ASC o DESC
}
