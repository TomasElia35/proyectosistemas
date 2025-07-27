package login.login.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INSUMOS")
public class Insumo {

    @Id
    @Column(name = "idInsumo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "codigoInterno", unique = true, nullable = false, length = 50)
    private String codigoInterno;

    @Column(name = "codigoBarras", length = 100)
    private String codigoBarras;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "modelo", length = 100)
    private String modelo;

    @Column(name = "numeroSerie", unique = true, length = 100)
    private String numeroSerie;

    @Column(name = "fechaCompra")
    private LocalDate fechaCompra;

    @Column(name = "precioCompra", precision = 12, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "fechaGarantia")
    private LocalDate fechaGarantia;

    @Column(name = "mesesGarantia")
    private Integer mesesGarantia;

    @Column(name = "especificacionesTecnicas", columnDefinition = "TEXT")
    private String especificacionesTecnicas;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "ubicacionFisica", length = 200)
    private String ubicacionFisica;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "creado")
    private LocalDateTime creado;

    @Column(name = "actualizado")
    private LocalDateTime actualizado;

    // Relaciones con otras entidades
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSector")
    private Sector sector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado", nullable = false)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuarioCreador")
    private Usuario usuarioCreador;

    @PrePersist
    public void prePersist() {
        if (creado == null) {
            creado = LocalDateTime.now();
        }
        actualizado = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        actualizado = LocalDateTime.now();
    }

    // Métodos de utilidad
    public boolean isEnGarantia() {
        return fechaGarantia != null && fechaGarantia.isAfter(LocalDate.now());
    }

    public boolean isOperativo() {
        return estado != null && "OPERATIVO".equals(estado.getNombre());
    }

    public boolean isActivo() {
        return activo != null && activo;
    }
}