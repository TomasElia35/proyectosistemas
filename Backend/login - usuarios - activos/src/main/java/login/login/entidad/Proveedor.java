package login.login.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombre;

    @Column(name = "cuit", nullable = false, unique = true, length = 15)
    @NotBlank(message = "El CUIT es obligatorio")
    private String cuit;

    @Column(name = "emailContacto", length = 150)
    private String emailContacto;

    @Column(name = "telefonoContacto", length = 20)
    private String telefonoContacto;

    @Column(name = "nombreContactoPrincipal", length = 100)
    private String nombreContactoPrincipal;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "formaPago")
    private FormaPago formaPago = FormaPago.CUENTA_CORRIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "monedaOperacion")
    private MonedaOperacion monedaOperacion = MonedaOperacion.ARS;

    @Column(name = "diasPagoPromedio")
    private Integer diasPagoPromedio = 30;

    @Column(name = "esActivo", nullable = false)
    private Boolean esActivo = true;

    @CreationTimestamp
    @Column(name = "fechaAlta", nullable = false)
    private LocalDateTime fechaAlta;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Relaciones inversas
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActivoTecnologico> activos;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MantenimientoActivo> mantenimientos;

    public enum FormaPago {
        CONTADO, CUENTA_CORRIENTE, CHEQUE, TRANSFERENCIA
    }

    public enum MonedaOperacion {
        ARS, USD, EUR
    }
}
