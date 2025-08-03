package login.login.DTO;

import login.login.entidad.TipoArticulo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroActivosDTO {
    private String codigoInventario;
    private String numeroSerie;
    private String nombre;
    private String marca;
    private String modelo;
    private Long idTipoArticulo;
    private TipoArticulo.CategoriaArticulo categoria;
    private Long idProveedor;
    private Long idEstado;
    private Long idArea;
    private LocalDate fechaAdquisicionDesde;
    private LocalDate fechaAdquisicionHasta;
    private BigDecimal costoMinimo;
    private BigDecimal costoMaximo;
    private Boolean esActivo;
    private Boolean soloConGarantiaVigente;
    private Boolean soloAsignados;
    private Boolean soloDisponibles;

    // Paginación
    private Integer pagina = 0;
    private Integer tamanio = 20;
    private String ordenarPor = "fechaCreacion";
    private String direccion = "DESC";
}