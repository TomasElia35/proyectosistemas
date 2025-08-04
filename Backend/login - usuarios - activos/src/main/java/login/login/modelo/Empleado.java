package login.login.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    private LocalDate fechaNacimiento;

    @Column(unique = true, length = 150)
    private String emailCorporativo;

    private String celularCorporativo;

    @ManyToOne
    @JoinColumn(name = "idArea")
    private Area area;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    private LocalDate fechaBaja;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoContrato tipoContrato;

    private Boolean activo = true;
    private Boolean esResponsableArea = false;

    @Lob
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum TipoContrato {
        EFECTIVO, EVENTUAL, PART_TIME, PASANTE, EXTERNO
    }
}
