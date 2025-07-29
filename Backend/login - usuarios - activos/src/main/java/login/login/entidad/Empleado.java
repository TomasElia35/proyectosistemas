package login.login.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "legajo", nullable = false, unique = true, length = 20)
    @NotBlank(message = "El legajo es obligatorio")
    private String legajo;

    @Column(name = "dni", nullable = false, unique = true, length = 12)
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @Column(name = "nombre", nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "emailCorporativo", unique = true, length = 150)
    private String emailCorporativo;

    @Column(name = "celularCorporativo", length = 20)
    private String celularCorporativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArea", nullable = false)
    @NotNull(message = "El área es obligatoria")
    private Area area;

    @Column(name = "fechaIngreso", nullable = false)
    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;

    @Column(name = "fechaBaja")
    private LocalDate fechaBaja;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoContrato", nullable = false)
    @NotNull(message = "El tipo de contrato es obligatorio")
    private TipoContrato tipoContrato;

    @Column(name = "esActivo", nullable = false)
    private Boolean esActivo = true;

    @Column(name = "esResponsableArea", nullable = false)
    private Boolean esResponsableArea = false;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Relaciones inversas
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AsignacionActivo> asignaciones;

    public enum TipoContrato {
        PLANTA, CONTRATO, PASANTE, TERCERO
    }

    // Método de conveniencia
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}