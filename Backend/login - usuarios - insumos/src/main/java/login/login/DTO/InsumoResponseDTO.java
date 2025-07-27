package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoResponseDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String codigoInterno;
    private String codigoBarras;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private LocalDate fechaCompra;
    private BigDecimal precioCompra;
    private LocalDate fechaGarantia;
    private Integer mesesGarantia;
    private String especificacionesTecnicas;
    private String observaciones;
    private String ubicacionFisica;
    private Boolean activo;
    private LocalDateTime creado;
    private LocalDateTime actualizado;

    // Información de entidades relacionadas
    private ProveedorSimpleDTO proveedor;
    private CategoriaSimpleDTO categoria;
    private SectorSimpleDTO sector;
    private EstadoSimpleDTO estado;
    private UsuarioSimpleDTO usuarioCreador;

    // Campos calculados
    private boolean enGarantia;
    private String estadoGarantia;
    private Integer diasGarantiaRestantes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProveedorSimpleDTO {
        private Integer id;
        private String nombreFantasia;
        private String razonSocial;
        private String email;
        private String telefono;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoriaSimpleDTO {
        private Integer id;
        private String nombre;
        private String descripcion;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SectorSimpleDTO {
        private Integer id;
        private String nombre;
        private String descripcion;
        private String responsable;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EstadoSimpleDTO {
        private Integer id;
        private String nombre;
        private String descripcion;
    }

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