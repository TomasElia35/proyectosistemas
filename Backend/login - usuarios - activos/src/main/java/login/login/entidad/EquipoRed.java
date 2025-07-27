package login.login.entidad;

import jakarta.persistence.*;
import login.login.modelo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipos_red")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoRed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEquipoRed")
    private Integer idEquipoRed;

    @Column(name = "codigo_activo", nullable = false, unique = true, length = 50)
    private String codigoActivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEquipoRed tipo;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 100)
    private String modelo;

    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    @Column(name = "direccion_ip", length = 15)
    private String direccionIp;

    @Column(name = "usuario_admin", length = 100)
    private String usuarioAdmin;

    @Column(name = "contraseña_admin", length = 255)
    private String contrasenaAdmin;

    @Column(name = "ubicacion_fisica", length = 200)
    private String ubicacionFisica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSector", referencedColumnName = "idSector")
    private Sector sector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado", referencedColumnName = "idEstado")
    private Estado estado;

    @Column(name = "fecha_instalacion")
    private LocalDate fechaInstalacion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por", referencedColumnName = "idUsuario")
    private Usuario creadoPor;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actualizado_por", referencedColumnName = "idUsuario")
    private Usuario actualizadoPor;

    public enum TipoEquipoRed {
        Router,
        Switch,
        AccessPoint("Access Point"),
        Otro;

        private final String displayName;

        TipoEquipoRed() {
            this.displayName = this.name();
        }

        TipoEquipoRed(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
