package login.login.DTO;

import jakarta.validation.constraints.NotBlank;
import login.login.entidad.Proveedor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El CUIT es obligatorio")
    private String cuit;

    private String emailContacto;
    private String telefonoContacto;
    private String nombreContactoPrincipal;
    private String direccion;
    private Proveedor.FormaPago formaPago;
    private Proveedor.MonedaOperacion monedaOperacion;
    private Integer diasPagoPromedio;
    private Boolean esActivo;
    private String observaciones;

    // Campos calculados
    private Integer cantidadActivosProvistos;
    private Integer cantidadMantenimientosRealizados;
}
