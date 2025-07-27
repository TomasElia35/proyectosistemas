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
@Table(name = "sistemas_empresariales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SistemaEmpresarial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSistema")
    private Integer idSistema;

    @Column(name = "nombre_sistema", nullable = false, length = 100)
    private String nombreSistema;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sistema", nullable = false)
    private TipoSistema tipoSistema;

    @Column(name = "url_acceso", length = 255)
    private String urlAcceso;

    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleado", referencedColumnName = "idEmpleado")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSector", referencedColumnName = "idSector")
    private Sector sector;

    @Column(name = "rol_permisos", length = 100)
    private String rolPermisos;

    @Column(name = "activo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo = true;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;

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

    public enum TipoSistema {
        CRM,
        ERP,
        Facturación("Facturación"),
        Contabilidad,
        Otro;

        private final String displayName;

        TipoSistema() {
            this.displayName = this.name();
        }

        TipoSistema(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}