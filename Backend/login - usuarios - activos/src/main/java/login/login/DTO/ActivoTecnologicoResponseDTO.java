package login.login.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivoTecnologicoResponseDTO {

    private Long id;

    private String codigoInventario;
    private String codigoTecnico;
    private String nombre;
    private String descripcion;

    private Long idModeloArticulo;
    private String modeloArticuloMarca;
    private String modeloArticuloModelo;

    private Long idProveedor;
    private String proveedorNombre;

    private BigDecimal costo;

    private LocalDate fechaAdquisicion;
    private Integer garantiaMeses;
    private LocalDate fechaVencimientoGarantia;

    private Long idEstado;
    private String estadoNombre;

    private Long idEmpleado;
    private String empleadoNombreCompleto;  // nombre + apellido
    private Long idArea;                    // del empleado
    private String areaNombre;             // del empleado

    private String observaciones;

    private String usuarioCreacion;
    private String usuarioModificacion;

    private Boolean activo;
    private LocalDate fechaCreacion;
    private LocalDate fechaUltimaModificacion;
}
