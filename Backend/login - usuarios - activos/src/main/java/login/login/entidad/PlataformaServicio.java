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
@Table(name = "plataformas_servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlataformaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlataforma")
    private Integer idPlataforma;

    @Column(name = "nombre_plataforma", nullable = false, length = 100)
    private String nombrePlataforma;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "email_asociado", length = 100)
    private String emailAsociado;

    @Column(name = "url_acceso", length = 255)
    private String urlAcceso;

    @Column(name = "tipo_cuenta", length = 50)
    private String tipoCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSector", referencedColumnName = "idSector")
    private Sector sector;

    @Column(name = "activo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo = true;

    @Column(name = "fecha_suscripcion")
    private LocalDate fechaSuscripcion;

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

    public enum TipoServicio {
        Pago,
        Streaming,
        RedSocial("Red Social"),
        Almacenamiento,
        Marketing,
        Bancario,
        Logística("Logística"),
        Otro;

        private final String displayName;

        TipoServicio() {
            this.displayName = this.name();
        }

        TipoServicio(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}