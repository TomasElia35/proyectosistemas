package login.login.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TipoArticulo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoArticulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre del tipo de artículo es obligatorio")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    @NotNull(message = "La categoría es obligatoria")
    private CategoriaArticulo categoria;

    @Column(name = "requiereNumeroSerie", nullable = false)
    private Boolean requiereNumeroSerie = true;

    @Column(name = "vidaUtilDefectoAnios", nullable = false)
    private Integer vidaUtilDefectoAnios = 3;

    @Column(name = "requiereMantenimiento", nullable = false)
    private Boolean requiereMantenimiento = false;

    @Column(name = "esActivo", nullable = false)
    private Boolean esActivo = true;

    @CreationTimestamp
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Relación inversa
    @OneToMany(mappedBy = "tipoArticulo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActivoTecnologico> activos;

    public enum CategoriaArticulo {
        HARDWARE, SOFTWARE, PERIFERICO, ACCESORIO
    }
}