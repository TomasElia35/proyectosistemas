package login.login.service;

import login.login.DTO.*;
import login.login.entidad.*;
import login.login.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MantenimientoActivoService {

    @Autowired
    private MantenimientoActivoRepository mantenimientoRepository;

    @Autowired
    private ActivoTecnologicoRepository activoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MovimientoActivoService movimientoService;

    public List<MantenimientoActivoDTO> listarTodos() {
        return mantenimientoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<MantenimientoActivoDTO> obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public List<MantenimientoActivoDTO> obtenerPorActivo(Long activoId) {
        return mantenimientoRepository.findByActivoIdOrderByFechaProgramadaDesc(activoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<MantenimientoActivoDTO> obtenerPendientes() {
        return mantenimientoRepository.findMantenimientosPendientes()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<MantenimientoActivoDTO> obtenerAtrasados() {
        return mantenimientoRepository.findMantenimientosAtrasados()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public MantenimientoActivoDTO programarMantenimiento(CrearMantenimientoDTO crearDTO) {
        // Validar que el activo existe
        ActivoTecnologico activo = activoRepository.findById(crearDTO.getIdActivo())
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));

        MantenimientoActivo mantenimiento = new MantenimientoActivo();
        mantenimiento.setActivo(activo);
        mantenimiento.setTipoMantenimiento(crearDTO.getTipoMantenimiento());
        mantenimiento.setFechaProgramada(crearDTO.getFechaProgramada());
        mantenimiento.setTecnicoAsignado(crearDTO.getTecnicoAsignado());
        mantenimiento.setCosto(crearDTO.getCosto());
        mantenimiento.setDescripcionProblema(crearDTO.getDescripcionProblema());
        mantenimiento.setHorasFueraServicio(crearDTO.getHorasFueraServicio());
        mantenimiento.setObservaciones(crearDTO.getObservaciones());
        mantenimiento.setUsuarioCreacion(crearDTO.getUsuarioCreacion());

        // Asignar proveedor si se especifica
        if (crearDTO.getIdProveedor() != null) {
            Proveedor proveedor = proveedorRepository.findById(crearDTO.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            mantenimiento.setProveedor(proveedor);
        }

        // Asignar estado resultante
        Estado estadoResultante = estadoRepository.findById(crearDTO.getIdEstadoResultante())
                .orElseThrow(() -> new RuntimeException("Estado resultante no encontrado"));
        mantenimiento.setEstadoResultante(estadoResultante);

        MantenimientoActivo mantenimientoGuardado = mantenimientoRepository.save(mantenimiento);

        // Si es mantenimiento correctivo, cambiar estado del activo a "En Mantenimiento"
        if (crearDTO.getTipoMantenimiento() == MantenimientoActivo.TipoMantenimiento.CORRECTIVO) {
            Estado estadoMantenimiento = EstadoRepository.findByNombre("En Mantenimiento")
                    .orElse(null);
            if (estadoMantenimiento != null) {
                Estado estadoAnterior = activo.getEstado();
                activo.setEstado(estadoMantenimiento);
                activoRepository.save(activo);

                // Registrar movimiento
                CrearMovimientoDTO movimientoDTO = new CrearMovimientoDTO();
                movimientoDTO.setIdActivo(activo.getId());
                movimientoDTO.setTipoMovimiento(MovimientoActivo.TipoMovimiento.MANTENIMIENTO);
                movimientoDTO.setIdEstadoAnterior(estadoAnterior.getId());
                movimientoDTO.setIdEstadoNuevo(estadoMantenimiento.getId());
                movimientoDTO.setMotivoMovimiento("Ingreso a mantenimiento " + crearDTO.getTipoMantenimiento().name().toLowerCase());
                movimientoDTO.setObservaciones(crearDTO.getDescripcionProblema());
                movimientoDTO.setUsuarioMovimiento(crearDTO.getUsuarioCreacion());

                movimientoService.registrarMovimiento(movimientoDTO);
            }
        }

        return convertirADTO(mantenimientoGuardado);
    }

    public MantenimientoActivoDTO completarMantenimiento(Long mantenimientoId, CompletarMantenimientoDTO completarDTO) {
        MantenimientoActivo mantenimiento = mantenimientoRepository.findById(mantenimientoId)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        if (mantenimiento.estaCompletado()) {
            throw new RuntimeException("El mantenimiento ya está completado");
        }

        // Actualizar datos del mantenimiento
        mantenimiento.setFechaRealizada(completarDTO.getFechaRealizada());
        mantenimiento.setDescripcionSolucion(completarDTO.getDescripcionSolucion());
        mantenimiento.setRepuestosUtilizados(completarDTO.getRepuestosUtilizados());
        mantenimiento.setCosto(completarDTO.getCosto());
        mantenimiento.setHorasFueraServicio(completarDTO.getHorasFueraServicio());

        // Actualizar estado resultante si es diferente
        Estado nuevoEstadoResultante = estadoRepository.findById(completarDTO.getIdEstadoResultante())
                .orElseThrow(() -> new RuntimeException("Estado resultante no encontrado"));
        mantenimiento.setEstadoResultante(nuevoEstadoResultante);

        if (completarDTO.getObservaciones() != null) {
            String observacionesActuales = mantenimiento.getObservaciones() != null ? mantenimiento.getObservaciones() : "";
            mantenimiento.setObservaciones(observacionesActuales + "\nCompletado: " + completarDTO.getObservaciones());
        }

        MantenimientoActivo mantenimientoActualizado = mantenimientoRepository.save(mantenimiento);

        // Cambiar estado del activo
        ActivoTecnologico activo = mantenimiento.getActivo();
        Estado estadoAnterior = activo.getEstado();
        activo.setEstado(nuevoEstadoResultante);
        activoRepository.save(activo);

        // Registrar movimiento
        CrearMovimientoDTO movimientoDTO = new CrearMovimientoDTO();
        movimientoDTO.setIdActivo(activo.getId());
        movimientoDTO.setTipoMovimiento(MovimientoActivo.TipoMovimiento.MANTENIMIENTO);
        movimientoDTO.setIdEstadoAnterior(estadoAnterior.getId());
        movimientoDTO.setIdEstadoNuevo(nuevoEstadoResultante.getId());
        movimientoDTO.setMotivoMovimiento("Finalización de mantenimiento " + mantenimiento.getTipoMantenimiento().name().toLowerCase());
        movimientoDTO.setObservaciones(completarDTO.getDescripcionSolucion());
        movimientoDTO.setUsuarioMovimiento("SISTEMA"); // Se podría parametrizar

        movimientoService.registrarMovimiento(movimientoDTO);

        return convertirADTO(mantenimientoActualizado);
    }

    private MantenimientoActivoDTO convertirADTO(MantenimientoActivo mantenimiento) {
        MantenimientoActivoDTO dto = new MantenimientoActivoDTO();

        dto.setId(mantenimiento.getId());
        dto.setIdActivo(mantenimiento.getActivo().getId());
        dto.setCodigoInventarioActivo(mantenimiento.getActivo().getCodigoInventario());
        dto.setNombreActivo(mantenimiento.getActivo().getNombre());
        dto.setTipoMantenimiento(mantenimiento.getTipoMantenimiento());
        dto.setFechaProgramada(mantenimiento.getFechaProgramada());
        dto.setFechaRealizada(mantenimiento.getFechaRealizada());

        if (mantenimiento.getProveedor() != null) {
            dto.setIdProveedor(mantenimiento.getProveedor().getId());
            dto.setNombreProveedor(mantenimiento.getProveedor().getNombre());
        }

        dto.setTecnicoAsignado(mantenimiento.getTecnicoAsignado());
        dto.setCosto(mantenimiento.getCosto());
        dto.setDescripcionProblema(mantenimiento.getDescripcionProblema());
        dto.setDescripcionSolucion(mantenimiento.getDescripcionSolucion());
        dto.setRepuestosUtilizados(mantenimiento.getRepuestosUtilizados());
        dto.setHorasFueraServicio(mantenimiento.getHorasFueraServicio());

        dto.setIdEstadoResultante(mantenimiento.getEstadoResultante().getId());
        dto.setNombreEstadoResultante(mantenimiento.getEstadoResultante().getNombre());

        dto.setObservaciones(mantenimiento.getObservaciones());
        dto.setUsuarioCreacion(mantenimiento.getUsuarioCreacion());
        dto.setFechaCreacion(mantenimiento.getFechaCreacion());

        // Campos calculados
        dto.setEstaCompletado(mantenimiento.estaCompletado());
        dto.setEstaAtrasado(mantenimiento.estaAtrasado());

        return dto;
    }
}