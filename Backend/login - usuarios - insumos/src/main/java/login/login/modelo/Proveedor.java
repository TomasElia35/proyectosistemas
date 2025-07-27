package login.login.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PROVEEDORES")
public class Proveedor {

    @Id
    @Column(name = "idProveedor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombreFantasia", nullable = false, length = 150)
    private String nombreFantasia;

    @Column(name = "razonSocial", length = 200)
    private String razonSocial;

    @Column(name = "cuit", unique = true, length = 15)
    private String cuit;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", length = 300)
    private String direccion;

    @Column(name = "medioDePago", length = 100)
    private String medioDePago;

    @Column(name = "contacto", length = 100)
    private String contacto;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "creado")
    private LocalDateTime creado;

    @Column(name = "actualizado")
    private LocalDateTime actualizado;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Insumo> insumos;

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
}