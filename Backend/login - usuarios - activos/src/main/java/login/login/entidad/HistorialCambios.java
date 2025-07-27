package login.login.entidad;

import jakarta.persistence.*;
import login.login.modelo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cambios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialCambios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHistorial")
    private Integer idHistorial;

    @Column(name = "tabla_afectada", nullable = false, length = 50)
    private String tablaAfectada;

    @Column(name = "id_registro", nullable = false)
    private Integer idRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacion", nullable = false)
    private TipoOperacion tipoOperacion;

    @Column(name = "datos_anteriores", columnDefinition = "JSON")
    private String datosAnteriores;

    @Column(name = "datos_nuevos", columnDefinition = "JSON")
    private String datosNuevos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "fecha_cambio", nullable = false, updatable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    public enum TipoOperacion {
        INSERT,
        UPDATE,
        DELETE
    }
}