package login.login.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 15)
    private String cuit;

    private String emailContacto;
    private String telefonoContacto;
    private String nombreContactoPrincipal;

    @Lob
    private String direccion;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago = FormaPago.CUENTA_CORRIENTE;

    @Enumerated(EnumType.STRING)
    private MonedaOperacion monedaOperacion = MonedaOperacion.ARS;

    private Integer diasPagoPromedio = 30;

    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta = LocalDateTime.now();

    @Lob
    private String observaciones;

    public enum FormaPago {
        CONTADO, CUENTA_CORRIENTE, CHEQUE, TRANSFERENCIA
    }

    public enum MonedaOperacion {
        ARS, USD
    }
}

