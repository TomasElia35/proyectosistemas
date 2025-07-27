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
@Table(name = "redes_wifi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedWifi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idWifi")
    private Integer idWifi;

    @Column(name = "nombre_red", nullable = false, length = 100)
    private String nombreRed;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "zona_ubicacion", length = 100)
    private String zonaUbicacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_seguridad", nullable = false)
    private TipoSeguridad tipoSeguridad = TipoSeguridad.WPA2;

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

    public enum TipoSeguridad {
        WPA2,
        WPA3,
        WEP,
        Abierta
    }
}