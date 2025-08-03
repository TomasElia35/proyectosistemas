package login.login.service;

import login.login.DTO.*;
import login.login.entidad.*;
import login.login.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoActivoService {

    @Autowired
    private MovimientoActivoRepository movimientoRepository;

    @Autowired
    private ActivoTecnologicoRepository activoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public List<MovimientoActivoDTO> listarTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<MovimientoActivoDTO> obtenerPorId(Long id) {
        return movimientoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public List<MovimientoActivoDTO> obtenerPorActivo(Long activoId) {
        return movimientoRepository.findByActivoIdOrderByFechaMovimientoDesc(activoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<MovimientoActivoDTO> obtenerPorTipo(MovimientoActivo.TipoMovimiento tipo) {
        return movimientoRepository.findByTipoMovimiento(tipo)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public MovimientoActivoDTO registrarMovimiento(CrearMovimientoDTO crearDTO) {
        // Validar que el activo existe
        ActivoTecnologico activo = activoRepository.findById(crearDTO.getIdActivo())
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));

        MovimientoActivo movimiento = new MovimientoActivo();
        movimiento.setActivo(activo);
        movimiento.setTipoMovimiento(crearDTO.getTipoMovimiento());
        movimiento.setFechaMovimiento(crearDTO.getFechaMovimiento());
        movimiento.setMotivoMovimiento(crearDTO.getMotivoMovimiento());
        movimiento.setObservaciones(crearDTO.getObservaciones());
        movimiento.setUsuarioMovimiento(crearDTO.getUsuarioMovimiento());

        // Asignar empleados si se especifican
        if (crearDTO.getIdEmpleadoOrigen() != null) {
            Empleado empleadoOrigen = empleadoRepository.findById(crearDTO.getIdEmpleadoOrigen())
                    .orElseThrow(() -> new RuntimeException("Empleado origen no encontrado"));
            movimiento.setEmpleadoOrigen(empleadoOrigen);
        }

        if (crearDTO.getIdEmpleadoDestino() != null) {
            Empleado empleadoDestino = empleadoRepository.findById(crearDTO.getIdEmpleadoDestino())
                    .orElseThrow(() -> new RuntimeException("Empleado destino no encontrado"));
            movimiento.setEmpleadoDestino(empleadoDestino);
        }

        // Asignar áreas si se especifican
        if (crearDTO.getIdAreaOrigen() != null) {
            Area areaOrigen = areaRepository.findById(crearDTO.getIdAreaOrigen())
                    .orElseThrow(() -> new RuntimeException("Área origen no encontrada"));
            movimiento.setAreaOrigen(areaOrigen);
        }

        if (crearDTO.getIdAreaDestino() != null) {
            Area areaDestino = areaRepository.findById(crearDTO.getIdAreaDestino())
                    .orElseThrow(() -> new RuntimeException("Área destino no encontrada"));
            movimiento.setAreaDestino(areaDestino);
        }

        // Asignar estados
        Estado estadoAnterior = estadoRepository.findById(crearDTO.getIdEstadoAnterior())
                .orElseThrow(() -> new RuntimeException("Estado anterior no encontrado"));
        movimiento.setEstadoAnterior(estadoAnterior);

        Estado estadoNuevo = estadoRepository.findById(crearDTO.getIdEstadoNuevo())
                .orElseThrow(() -> new RuntimeException("Estado nuevo no encontrado"));
        movimiento.setEstadoNuevo(estadoNuevo);

        MovimientoActivo movimientoGuardado = movimientoRepository.save(movimiento);
        return convertirADTO(movimientoGuardado);
    }

    private MovimientoActivoDTO convertirADTO(MovimientoActivo movimiento) {
        MovimientoActivoDTO dto = new MovimientoActivoDTO();

        dto.setId(movimiento.getId());
        dto.setIdActivo(movimiento.getActivo().getId());
        dto.setCodigoInventarioActivo(movimiento.getActivo().getCodigoInventario());
        dto.setNombreActivo(movimiento.getActivo().getNombre());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());

        // Empleados
        if (movimiento.getEmpleadoOrigen() != null) {
            dto.setIdEmpleadoOrigen(movimiento.getEmpleadoOrigen().getId());
            dto.setNombreEmpleadoOrigen(movimiento.getEmpleadoOrigen().getNombreCompleto());
        }

        if (movimiento.getEmpleadoDestino() != null) {
            dto.setIdEmpleadoDestino(movimiento.getEmpleadoDestino().getId());
            dto.setNombreEmpleadoDestino(movimiento.getEmpleadoDestino().getNombreCompleto());
        }

        // Áreas
        if (movimiento.getAreaOrigen() != null) {
            dto.setIdAreaOrigen(movimiento.getAreaOrigen().getId());
            dto.setNombreAreaOrigen(movimiento.getAreaOrigen().getNombre());
        }

        if (movimiento.getAreaDestino() != null) {
            dto.setIdAreaDestino(movimiento.getAreaDestino().getId());
            dto.setNombreAreaDestino(movimiento.getAreaDestino().getNombre());
        }

        // Estados
        dto.setIdEstadoAnterior(movimiento.getEstadoAnterior().getId());
        dto.setNombreEstadoAnterior(movimiento.getEstadoAnterior().getNombre());
        dto.setIdEstadoNuevo(movimiento.getEstadoNuevo().getId());
        dto.setNombreEstadoNuevo(movimiento.getEstadoNuevo().getNombre());

        dto.setMotivoMovimiento(movimiento.getMotivoMovimiento());
        dto.setObservaciones(movimiento.getObservaciones());
        dto.setUsuarioMovimiento(movimiento.getUsuarioMovimiento());
        dto.setFechaCreacion(movimiento.getFechaCreacion());

        return dto;
    }
}
