package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoFiltroDTO {

    private String nombre;
    private String codigoInterno;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private Integer idCategoria;
    private String categoria; // Para compatibilidad
    private Integer idProveedor;
    private String proveedor; // Para compatibilidad
    private Integer idSector;
    private Integer idEstado;
    private Boolean activo;
    private LocalDate fechaCompraDesde;
    private LocalDate fechaCompraHasta;
    private Boolean soloEnGarantia;
    private Boolean soloFueraGarantia;
    private Boolean soloOperativos;
    private String ubicacionFisica;
}