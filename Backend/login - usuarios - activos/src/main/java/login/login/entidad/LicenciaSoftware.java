package login.login.entidad;

import jakarta.persistence.*;
import login.login.modelo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "licencias_software")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenciaSoftware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLicencia")
    private Integer idLicencia;

    @Column(name = "nombre_software", nullable = false, length = 100)
    private String nombreSoftware;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_software", nullable = false)
    private TipoSoftware tipoSoftware;

    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "clave_producto", length = 255)
    private String claveProducto;

    @Column(name = "clave_licencia", length = 255)
    private String claveLicencia;

    @Column(name = "email_asociado", length = 100)
    private String emailAsociado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_licencia", nullable = false)
    private TipoLicencia tipoLicencia = TipoLicencia.Individual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleado", referencedColumnName = "idEmpleado")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSector", referencedColumnName = "idSector")
    private Sector sector;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "activo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo = true;

    @Column(name = "fecha_instalacion")
    private LocalDate fechaInstalacion;

    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "proveedor", length = 100)
    private String proveedor;

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

    public enum TipoSoftware {
        Office,
        Antivirus,
        Adobe,
        AutoCAD,
        Especializado,
        Otro
    }

    public enum TipoLicencia {
        Individual,
        Empresarial,
        Familiar,
        Educacional
    }
}
