package login.login.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TipoArticulo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoArticulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Lob
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Boolean requiereNumeroSerie = true;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum Categoria {
        HARDWARE, SOFTWARE, PERIFERICO, ACCESORIO
    }
}
