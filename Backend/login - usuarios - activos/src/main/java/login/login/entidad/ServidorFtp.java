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
@Table(name = "servidores_ftp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorFtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idServidor")
    private Integer idServidor;

    @Column(name = "nombre_servidor", nullable = false, length = 100)
    private String nombreServidor;

    @Column(name = "direccion_ip", length = 15)
    private String direccionIp;

    @Column(name = "url_servidor", length = 255)
    private String urlServidor;

    @Column(name = "puerto", nullable = false)
    private Integer puerto = 21;

    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

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