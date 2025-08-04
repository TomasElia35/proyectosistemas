package login.login.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Area")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Lob
    private String descripcion;

    private Integer idResponsable; // o relación futura con Empleado

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UbicacionFisica ubicacionFisica;

    private Boolean activa = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum UbicacionFisica {
        HERGO, ALTA_DISTRIBUCION, SALON, DISTRITINO, COUSTLAIN,
        MENOR_COSTE_ALBERTI, MENOR_COSTE_ALEM, MENOR_COSTE_CORDOBA,
        MENOR_COSTE_CARILO, LA_CHATA
    }
}
