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
@Table(name = "correos_electronicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorreoElectronico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCorreo")
    private Integer idCorreo;

    @Column(name = "direccion_email", nullable = false, unique = true, length = 100)
    private String direccionEmail;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleado", referencedColumnName = "idEmpleado")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSector", referencedColumnName = "idSector")
    private Sector sector;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private TipoCuenta tipoCuenta = TipoCuenta.Personal;

    @Column(name = "activo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo = true;

    @Column(name = "fecha_creacion_cuenta")
    private LocalDate fechaCreacionCuenta;

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

    public enum TipoCuenta {
        Personal,
        Compartida
    }
}