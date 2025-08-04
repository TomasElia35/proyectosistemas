package login.login.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ModeloArticulo", uniqueConstraints = @UniqueConstraint(columnNames = {"marca", "modelo"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloArticulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idTipoArticulo")
    private TipoArticulo tipoArticulo;
}
