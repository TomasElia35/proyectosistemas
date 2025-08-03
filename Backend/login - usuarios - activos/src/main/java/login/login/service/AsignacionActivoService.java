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
public class AsignacionActivoService {

    @Autowired
    private AsignacionActivoRepository asignacionRepository;

    @Autowired
    private ActivoTecnologicoRepository activoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MovimientoActivoService movimientoService;

    public List<AsignacionActivoDTO> listarTodas() {
        return asignacionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<AsignacionActivoDTO> obtenerPorId(Long id) {
        return asignacionRepository.findById(id)
                .map(this::convertirADTO);
    }

    public List<AsignacionActivoDTO> obtenerAsignacionesActivas() {
        return asignacionRepository.findAll()
                .stream()
                .filter(AsignacionActivo::esAsignacionActiva)
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<AsignacionActivoDTO> obtenerPorEmpleado(Long empleadoId) {
        return asignacionRepository.findByEmpleadoIdOrderByFechaAsignacionDesc(empleadoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public static List<AsignacionActivoDTO> obtenerActivasPorEmpleado(Long empleadoId) {
        return AsignacionActivoRepository.findAsignacionesActivasByEmpleadoId(empleadoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public static AsignacionActivoDTO asignarActivo(CrearAsignacionDTO crearDTO) {
        // Validar que el activo existe
        ActivoTecnologico activo = ActivoTecnologicoRepository.findById(crearDTO.getIdActivo())
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));

        // Validar que el activo está disponible
        Optional<AsignacionActivo> asignacionExistente = AsignacionActivoRepository.findAsignacionActivaByActivoId(crearDTO.getIdActivo());
        if (asignacionExistente.isPresent()) {
            throw new RuntimeException("El activo ya está asignado");
        }

        // Validar que el estado permite asignación
        if (!activo.getEstado().getPermiteAsignacion()) {
            throw new RuntimeException("El estado actual del activo no permite asignación");
        }

        // Validar que el empleado existe
        Empleado empleado = EmpleadoRepository.findById(crearDTO.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        if (!empleado.getEsActivo()) {
            throw new RuntimeException("No se puede asignar activos a empleados inactivos");
        }

        // Validar que el área existe
        Area area = AreaRepository.findById(crearDTO.getIdArea())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        // Crear la asignación
        AsignacionActivo asignacion = new AsignacionActivo();
        asignacion.setActivo(activo);
        asignacion.setEmpleado(empleado);
        asignacion.setArea(area);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setObservaciones(crearDTO.getObservaciones());
        asignacion.setUsuarioAsignacion(crearDTO.getUsuarioAsignacion());

        AsignacionActivo asignacionGuardada = AsignacionActivoRepository.save(asignacion);

        // Cambiar estado del activo a "Asignado"
        Estado estadoAsignado = EstadoRepository.findByNombre("Asignado")
                .orElseThrow(() -> new RuntimeException("Estado 'Asignado' no encontrado"));

        Estado estadoAnterior = activo.getEstado();
        activo.setEstado(estadoAsignado);
        ActivoTecnologicoRepository.save(activo);

        // Registrar movimiento
        CrearMovimientoDTO movimientoDTO = new CrearMovimientoDTO();
        movimientoDTO.setIdActivo(activo.getId());
        movimientoDTO.setTipoMovimiento(MovimientoActivo.TipoMovimiento.ASIGNACION);
        movimientoDTO.setIdEmpleadoDestino(empleado.getId());
        movimientoDTO.setIdAreaDestino(area.getId());
        movimientoDTO.setIdEstadoAnterior(estadoAnterior.getId());
        movimientoDTO.setIdEstadoNuevo(estadoAsignado.getId());
        movimientoDTO.setMotivoMovimiento("Asignación de activo a empleado");
        movimientoDTO.setObservaciones(crearDTO.getObservaciones());
        movimientoDTO.setUsuarioMovimiento(crearDTO.getUsuarioAsignacion());

        MovimientoActivoRepository.registrarMovimiento(movimientoDTO);

        return convertirADTO(asignacionGuardada);
    }

    public static void desasignarActivo(Long asignacionId, DesasignarActivoDTO desasignarDTO) {
        AsignacionActivo asignacion = AsignacionActivoRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (!asignacion.esAsignacionActiva()) {
            throw new RuntimeException("La asignación ya fue finalizada");
        }

        // Finalizar la asignación
        asignacion.setFechaDesasignacion(LocalDateTime.now());
        asignacion.setMotivoDesasignacion(desasignarDTO.getMotivoDesasignacion());
        if (desasignarDTO.getObservaciones() != null) {
            String observacionesActuales = asignacion.getObservaciones() != null ? asignacion.getObservaciones() : "";
            asignacion.setObservaciones(observacionesActuales + "\nDesasignación: " + desasignarDTO.getObservaciones());
        }

        AsignacionActivoRepository.save(asignacion);

        // Cambiar estado del activo a "Disponible"
        ActivoTecnologico activo = asignacion.getActivo();
        Estado estadoAnterior = activo.getEstado();
        Estado estadoDisponible = EstadoRepository.findByNombre("Disponible")
                .orElseThrow(() -> new RuntimeException("Estado 'Disponible' no encontrado"));

        activo.setEstado(estadoDisponible);
        ActivoTecnologicoRepository.save(activo);

        // Registrar movimiento
        CrearMovimientoDTO movimientoDTO = new CrearMovimientoDTO();
        movimientoDTO.setIdActivo(activo.getId());
        movimientoDTO.setTipoMovimiento(MovimientoActivo.TipoMovimiento.DEVOLUCION);
        movimientoDTO.setIdEmpleadoOrigen(asignacion.getEmpleado().getId());
        movimientoDTO.setIdAreaOrigen(asignacion.getArea().getId());
        movimientoDTO.setIdEstadoAnterior(estadoAnterior.getId());
        movimientoDTO.setIdEstadoNuevo(estadoDisponible.getId());
        movimientoDTO.setMotivoMovimiento(desasignarDTO.getMotivoDesasignacion());
        movimientoDTO.setObservaciones(desasignarDTO.getObservaciones());
        movimientoDTO.setUsuarioMovimiento(desasignarDTO.getUsuario());

        MovimientoActivoRepository.registrarMovimiento(movimientoDTO);
    }

    private AsignacionActivoDTO convertirADTO(AsignacionActivo asignacion) {
        AsignacionActivoDTO dto = new AsignacionActivoDTO();

        dto.setId(asignacion.getId());
        dto.setIdActivo(asignacion.getActivo().getId());
        dto.setCodigoInventarioActivo(asignacion.getActivo().getCodigoInventario());
        dto.setNombreActivo(asignacion.getActivo().getNombre());

        dto.setIdEmpleado(asignacion.getEmpleado().getId());
        dto.setLegajoEmpleado(asignacion.getEmpleado().getLegajo());
        dto.setNombreCompletoEmpleado(asignacion.getEmpleado().getNombreCompleto());

        dto.setIdArea(asignacion.getArea().getId());
        dto.setNombreArea(asignacion.getArea().getNombre());

        dto.setFechaAsignacion(asignacion.getFechaAsignacion());
        dto.setFechaDesasignacion(asignacion.getFechaDesasignacion());
        dto.setMotivoDesasignacion(asignacion.getMotivoDesasignacion());
        dto.setObservaciones(asignacion.getObservaciones());
        dto.setUsuarioAsignacion(asignacion.getUsuarioAsignacion());
        dto.setFechaCreacion(asignacion.getFechaCreacion());

        // Campo calculado
        dto.setEsAsignacionActiva(asignacion.esAsignacionActiva());

        return dto;
    }
}