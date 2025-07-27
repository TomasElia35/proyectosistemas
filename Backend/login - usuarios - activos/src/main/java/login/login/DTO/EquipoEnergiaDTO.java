package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoEnergiaDTO {

    private Integer idEquipoEnergia;
    private String codigoActivo;
    private String tipo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String potencia;
    private String ubicacionFisica;
    private Integer sectorId;
    private String sectorNombre;
    private Integer estadoId;
    private String estadoNombre;
    private LocalDate fechaInstalacion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private Integer creadoPorId;
    private String creadoPorNombre;
    private LocalDateTime fechaActualizacion;
    private Integer actualizadoPorId;
    private String actualizadoPorNombre;
}