package login.login.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ESTADOS")
public class Estado {

    @Id
    @Column(name = "idEstado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "creado")
    private LocalDateTime creado;

    @Column(name = "actualizado")
    private LocalDateTime actualizado;

    @OneToMany(mappedBy = "estado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Insumo> insumos;

    @PrePersist
    public void prePersist() {
        if (creado == null) {
            creado = LocalDateTime.now();
        }
        actualizado = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        actualizado = LocalDateTime.now();
    }
}