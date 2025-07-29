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
@Table(name = "Estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "permiteAsignacion", nullable = false)
    private Boolean permiteAsignacion = true;

    @Column(name = "requiereAprobacion", nullable = false)
    private Boolean requiereAprobacion = false;

    @Column(name = "esEstadoFinal", nullable = false)
    private Boolean esEstadoFinal = false;

    @Column(name = "esActivo", nullable = false)
    private Boolean esActivo = true;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Relaciones inversas
    @OneToMany(mappedBy = "estado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActivoTecnologico> activos;
}
