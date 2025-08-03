package login.login.service;

import login.login.DTO.*;
import login.login.entidad.*;
import login.login.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivoTecnologicoService {

    @Autowired
    private ActivoTecnologicoRepository activoRepository;

    @Autowired
    private TipoArticuloRepository tipoArticuloRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AsignacionActivoRepository asignacionRepository;

    public List<ActivoTecnologicoDTO> listarTodos() {
        return activoRepository.findByEsActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<ActivoTecnologicoDTO> obtenerPorId(Long id) {
        return activoRepository.findById(id)
                .filter(activo -> activo.getEsActivo())
                .map(this::convertirADTO);
    }

    public Optional<ActivoTecnologicoDTO> obtenerPorCodigoInventario(String codigoInventario) {
        return activoRepository.findByCodigoInventario(codigoInventario)
                .filter(activo -> activo.getEsActivo())
                .map(this::convertirADTO);
    }

    public ActivoTecnologicoDTO crear(CrearActivoTecnologicoDTO crearDTO) {
        // Validar que no exista el código de inventario
        if (activoRepository.findByCodigoInventario(crearDTO.getCodigoInventario()).isPresent()) {
            throw new RuntimeException("Ya existe un activo con el código de inventario: " + crearDTO.getCodigoInventario());
        }

        // Validar número de serie si se proporciona
        if (crearDTO.getNumeroSerie() != null && !crearDTO.getNumeroSerie().trim().isEmpty()) {
            if (activoRepository.findByNumeroSerie(crearDTO.getNumeroSerie()).isPresent()) {
                throw new RuntimeException("Ya existe un activo con el número de serie: " + crearDTO.getNumeroSerie());
            }
        }

        ActivoTecnologico activo = new ActivoTecnologico();
        mapearCrearDTOAEntidad(crearDTO, activo);

        // Calcular fecha de vencimiento de garantía
        if (crearDTO.getGarantiaMeses() != null && crearDTO.getGarantiaMeses() > 0) {
            activo.setFechaVencimientoGarantia(
                    crearDTO.getFechaAdquisicion().plusMonths(crearDTO.getGarantiaMeses())
            );
        }

        // Si no se especifica valor en libros, usar el costo
        if (activo.getValorLibros() == null) {
            activo.setValorLibros(activo.getCosto());
        }

        ActivoTecnologico activoGuardado = activoRepository.save(activo);
        return convertirADTO(activoGuardado);
    }

    public ActivoTecnologicoDTO actualizar(Long id, ActualizarActivoTecnologicoDTO actualizarDTO) {
        ActivoTecnologico activo = activoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));

        if (!activo.getEsActivo()) {
            throw new RuntimeException("No se puede actualizar un activo inactivo");
        }

        mapearActualizarDTOAEntidad(actualizarDTO, activo);
        activo.setFechaUltimaModificacion(LocalDateTime.now());

        ActivoTecnologico activoActualizado = activoRepository.save(activo);
        return convertirADTO(activoActualizado);
    }

    public void eliminar(Long id) {
        ActivoTecnologico activo = activoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));

        // Verificar que no tenga asignaciones activas
        Optional<AsignacionActivo> asignacionActiva = AsignacionActivoRepository.findAsignacionActivaByActivoId(id);
        if (asignacionActiva.isPresent()) {
            throw new RuntimeException("No se puede eliminar un activo que tiene asignaciones activas");
        }

        activo.setEsActivo(false);
        activoRepository.save(activo);
    }

    public Page<ActivoTecnologicoDTO> buscarConFiltros(FiltroActivosDTO filtros) {
        Sort sort = Sort.by(Sort.Direction.fromString(filtros.getDireccion()), filtros.getOrdenarPor());
        Pageable pageable = PageRequest.of(filtros.getPagina(), filtros.getTamanio(), sort);

        Page<ActivoTecnologico> activosPage = activoRepository.findByFiltros(
                filtros.getCodigoInventario(),
                filtros.getNumeroSerie(),
                filtros.getNombre(),
                filtros.getMarca(),
                filtros.getModelo(),
                filtros.getIdTipoArticulo(),
                filtros.getIdProveedor(),
                filtros.getIdEstado(),
                filtros.getIdArea(),
                filtros.getFechaAdquisicionDesde(),
                filtros.getFechaAdquisicionHasta(),
                filtros.getCostoMinimo(),
                filtros.getCostoMaximo(),
                filtros.getEsActivo(),
                pageable
        );

        return activosPage.map(this::convertirADTO);
    }

    public List<ActivoTecnologicoDTO> obtenerActivosDisponibles() {
        return activoRepository.findActivosDisponibles()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ActivoTecnologicoDTO> obtenerActivosAsignados() {
        return activoRepository.findActivosAsignados()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ResumenActivosDTO obtenerResumen() {
        ResumenActivosDTO resumen = new ResumenActivosDTO();

        resumen.setTotalActivos(activoRepository.countTotalActivos());
        resumen.setActivosDisponibles((long) activoRepository.findActivosDisponibles().size());
        resumen.setActivosAsignados((long) activoRepository.findActivosAsignados().size());
        resumen.setActivosConGarantiaVigente((long) activoRepository.findActivosConGarantiaVigente().size());

        resumen.setActivosHardware(activoRepository.countByCategoria(TipoArticulo.CategoriaArticulo.HARDWARE));
        resumen.setActivosSoftware(activoRepository.countByCategoria(TipoArticulo.CategoriaArticulo.SOFTWARE));
        resumen.setActivosPeriferico(activoRepository.countByCategoria(TipoArticulo.CategoriaArticulo.PERIFERICO));
        resumen.setActivosAccesorio(activoRepository.countByCategoria(TipoArticulo.CategoriaArticulo.ACCESORIO));

        resumen.setValorTotalActivos(activoRepository.sumTotalCosto());
        resumen.setValorTotalEnLibros(activoRepository.sumTotalValorLibros());

        return resumen;
    }

    private ActivoTecnologicoDTO convertirADTO(ActivoTecnologico activo) {
        ActivoTecnologicoDTO dto = new ActivoTecnologicoDTO();

        dto.setId(activo.getId());
        dto.setCodigoInventario(activo.getCodigoInventario());
        dto.setNumeroSerie(activo.getNumeroSerie());
        dto.setNombre(activo.getNombre());
        dto.setDescripcion(activo.getDescripcion());
        dto.setMarca(activo.getMarca());
        dto.setModelo(activo.getModelo());

        dto.setIdTipoArticulo(activo.getTipoArticulo().getId());
        dto.setNombreTipoArticulo(activo.getTipoArticulo().getNombre());
        dto.setCategoriaTipoArticulo(activo.getTipoArticulo().getCategoria().name());

        dto.setIdProveedor(activo.getProveedor().getId());
        dto.setNombreProveedor(activo.getProveedor().getNombre());

        dto.setCosto(activo.getCosto());
        dto.setFechaAdquisicion(activo.getFechaAdquisicion());
        dto.setGarantiaMeses(activo.getGarantiaMeses());
        dto.setFechaVencimientoGarantia(activo.getFechaVencimientoGarantia());
        dto.setVidaUtilAnios(activo.getVidaUtilAnios());
        dto.setValorLibros(activo.getValorLibros());
        dto.setUbicacionFisica(activo.getUbicacionFisica());
        dto.setEspecificacionesTecnicas(activo.getEspecificacionesTecnicas());

        dto.setIdEstado(activo.getEstado().getId());
        dto.setNombreEstado(activo.getEstado().getNombre());
        dto.setPermiteAsignacion(activo.getEstado().getPermiteAsignacion());

        dto.setIdArea(activo.getArea().getId());
        dto.setNombreArea(activo.getArea().getNombre());

        dto.setObservaciones(activo.getObservaciones());
        dto.setFechaCreacion(activo.getFechaCreacion());
        dto.setFechaUltimaModificacion(activo.getFechaUltimaModificacion());
        dto.setUsuarioCreacion(activo.getUsuarioCreacion());
        dto.setUsuarioModificacion(activo.getUsuarioModificacion());
        dto.setEsActivo(activo.getEsActivo());

        // Campos calculados
        dto.setTieneGarantiaVigente(activo.tieneGarantiaVigente());
        dto.setIdentificacion(activo.getIdentificacion());

        // Obtener asignación activa
        Optional<AsignacionActivo> asignacionActiva = AsignacionActivoRepository.findAsignacionActivaByActivoId(activo.getId());
        if (asignacionActiva.isPresent()) {
            AsignacionActivo asignacion = asignacionActiva.get();
            AsignacionActivaDTO asignacionDTO = new AsignacionActivaDTO();
            asignacionDTO.setIdAsignacion(asignacion.getId());
            asignacionDTO.setIdEmpleado(asignacion.getEmpleado().getId());
            asignacionDTO.setLegajoEmpleado(asignacion.getEmpleado().getLegajo());
            asignacionDTO.setNombreCompletoEmpleado(asignacion.getEmpleado().getNombreCompleto());
            asignacionDTO.setEmailEmpleado(asignacion.getEmpleado().getEmailCorporativo());
            asignacionDTO.setIdArea(asignacion.getArea().getId());
            asignacionDTO.setNombreArea(asignacion.getArea().getNombre());
            asignacionDTO.setFechaAsignacion(asignacion.getFechaAsignacion());
            asignacionDTO.setUsuarioAsignacion(asignacion.getUsuarioAsignacion());
            dto.setAsignacionActiva(asignacionDTO);
        }

        return dto;
    }

    private void mapearCrearDTOAEntidad(CrearActivoTecnologicoDTO dto, ActivoTecnologico activo) {
        activo.setCodigoInventario(dto.getCodigoInventario());
        activo.setNumeroSerie(dto.getNumeroSerie());
        activo.setNombre(dto.getNombre());
        activo.setDescripcion(dto.getDescripcion());
        activo.setMarca(dto.getMarca());
        activo.setModelo(dto.getModelo());
        activo.setCosto(dto.getCosto());
        activo.setFechaAdquisicion(dto.getFechaAdquisicion());
        activo.setGarantiaMeses(dto.getGarantiaMeses());
        activo.setVidaUtilAnios(dto.getVidaUtilAnios());
        activo.setValorLibros(dto.getValorLibros());
        activo.setUbicacionFisica(dto.getUbicacionFisica());
        activo.setEspecificacionesTecnicas(dto.getEspecificacionesTecnicas());
        activo.setObservaciones(dto.getObservaciones());
        activo.setUsuarioCreacion(dto.getUsuarioCreacion());

        // Obtener entidades relacionadas
        TipoArticulo tipoArticulo = tipoArticuloRepository.findById(dto.getIdTipoArticulo())
                .orElseThrow(() -> new RuntimeException("Tipo de artículo no encontrado"));
        activo.setTipoArticulo(tipoArticulo);

        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        activo.setProveedor(proveedor);

        Area area = areaRepository.findById(dto.getIdArea())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));
        activo.setArea(area);

        // Estado por defecto (se debe crear un estado "Disponible" o similar)
        Estado estadoDisponible = EstadoRepository.findByNombre("Disponible")
                .orElseThrow(() -> new RuntimeException("Estado 'Disponible' no encontrado"));
        activo.setEstado(estadoDisponible);
    }

    private void mapearActualizarDTOAEntidad(ActualizarActivoTecnologicoDTO dto, ActivoTecnologico activo) {
        if (dto.getNumeroSerie() != null) activo.setNumeroSerie(dto.getNumeroSerie());
        if (dto.getNombre() != null) activo.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) activo.setDescripcion(dto.getDescripcion());
        if (dto.getMarca() != null) activo.setMarca(dto.getMarca());
        if (dto.getModelo() != null) activo.setModelo(dto.getModelo());
        if (dto.getCosto() != null) activo.setCosto(dto.getCosto());
        if (dto.getFechaAdquisicion() != null) activo.setFechaAdquisicion(dto.getFechaAdquisicion());
        if (dto.getGarantiaMeses() != null) {
            activo.setGarantiaMeses(dto.getGarantiaMeses());
            if (dto.getGarantiaMeses() > 0) {
                activo.setFechaVencimientoGarantia(
                        activo.getFechaAdquisicion().plusMonths(dto.getGarantiaMeses())
                );
            }
        }
        if (dto.getVidaUtilAnios() != null) activo.setVidaUtilAnios(dto.getVidaUtilAnios());
        if (dto.getValorLibros() != null) activo.setValorLibros(dto.getValorLibros());
        if (dto.getUbicacionFisica() != null) activo.setUbicacionFisica(dto.getUbicacionFisica());
        if (dto.getEspecificacionesTecnicas() != null) activo.setEspecificacionesTecnicas(dto.getEspecificacionesTecnicas());
        if (dto.getObservaciones() != null) activo.setObservaciones(dto.getObservaciones());
        activo.setUsuarioModificacion(dto.getUsuarioModificacion());

        if (dto.getIdTipoArticulo() != null) {
            TipoArticulo tipoArticulo = tipoArticuloRepository.findById(dto.getIdTipoArticulo())
                    .orElseThrow(() -> new RuntimeException("Tipo de artículo no encontrado"));
            activo.setTipoArticulo(tipoArticulo);
        }

        if (dto.getIdProveedor() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            activo.setProveedor(proveedor);
        }

        if (dto.getIdArea() != null) {
            Area area = areaRepository.findById(dto.getIdArea())
                    .orElseThrow(() -> new RuntimeException("Área no encontrada"));
            activo.setArea(area);
        }
    }
}