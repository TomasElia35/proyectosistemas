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
@Table(name = "Area")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre del área es obligatorio")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "centroCosto", length = 50)
    private String centroCosto;

    @Column(name = "idResponsable")
    private Long idResponsable; // FK a tabla usuario

    @Column(name = "ubicacionFisica", length = 200)
    private String ubicacionFisica;

    @Column(name = "esActiva", nullable = false)
    private Boolean esActiva = true;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Relaciones inversas
    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActivoTecnologico> activos;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Empleado> empleados;
}