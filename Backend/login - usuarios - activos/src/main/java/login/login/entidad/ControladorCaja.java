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
@Table(name = "controladores_cajas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControladorCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idControlador")
    private Integer idControlador;

    @Column(name = "codigo_controlador", nullable = false, unique = true, length = 50)
    private String codigoControlador;

    @Column(name = "direccion_ip", nullable = false, length = 15)
    private String direccionIp;

    @Column(name = "usuario", length = 100)
    private String usuario;

    @Column(name = "contraseña", length = 255)
    private String contrasena;

    @Column(name = "ubicacion_fisica", length = 200)
    private String ubicacionFisica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSector", referencedColumnName = "idSector")
    private Sector sector;

    @Column(name = "activo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo = true;

    @Column(name = "fecha_configuracion")
    private LocalDate fechaConfiguracion;

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
}