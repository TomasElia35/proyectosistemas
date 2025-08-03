package login.login.service;

import login.login.DTO.EmpleadoDTO;
import login.login.entidad.Area;
import login.login.entidad.Empleado;
import login.login.repository.AreaRepository;
import login.login.repository.AsignacionActivoRepository;
import login.login.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AsignacionActivoRepository asignacionRepository;

    public List<EmpleadoDTO> listarTodos() {
        return empleadoRepository.findByEsActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpleadoDTO> obtenerPorId(Long id) {
        return empleadoRepository.findById(id)
                .filter(Empleado::getEsActivo)
                .map(this::convertirADTO);
    }

    public Optional<EmpleadoDTO> obtenerPorLegajo(String legajo) {
        return empleadoRepository.findByLegajo(legajo)
                .filter(Empleado::getEsActivo)
                .map(this::convertirADTO);
    }

    public List<EmpleadoDTO> buscarEmpleados(String busqueda) {
        return empleadoRepository.buscarEmpleados(busqueda)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public EmpleadoDTO crear(EmpleadoDTO empleadoDTO) {
        // Validaciones
        if (empleadoRepository.findByLegajo(empleadoDTO.getLegajo()).isPresent()) {
            throw new RuntimeException("Ya existe un empleado con el legajo: " + empleadoDTO.getLegajo());
        }

        if (empleadoRepository.findByDni(empleadoDTO.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un empleado con el DNI: " + empleadoDTO.getDni());
        }

        if (empleadoDTO.getEmailCorporativo() != null &&
                empleadoRepository.findByEmailCorporativo(empleadoDTO.getEmailCorporativo()).isPresent()) {
            throw new RuntimeException("Ya existe un empleado con el email: " + empleadoDTO.getEmailCorporativo());
        }

        Empleado empleado = new Empleado();
        mapearDTOAEntidad(empleadoDTO, empleado);

        Empleado empleadoGuardado = empleadoRepository.save(empleado);
        return convertirADTO(empleadoGuardado);
    }

    public EmpleadoDTO actualizar(Long id, EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        if (!empleado.getEsActivo()) {
            throw new RuntimeException("No se puede actualizar un empleado inactivo");
        }

        // Validar unicidad de campos
        Optional<Empleado> empleadoPorLegajo = empleadoRepository.findByLegajo(empleadoDTO.getLegajo());
        if (empleadoPorLegajo.isPresent() && !empleadoPorLegajo.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe un empleado con el legajo: " + empleadoDTO.getLegajo());
        }

        mapearDTOAEntidad(empleadoDTO, empleado);

        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        return convertirADTO(empleadoActualizado);
    }

    public void eliminar(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Verificar que no tenga asignaciones activas
        Long asignacionesActivas = asignacionRepository.countAsignacionesActivasByEmpleadoId(id);
        if (asignacionesActivas > 0) {
            throw new RuntimeException("No se puede eliminar un empleado que tiene activos asignados");
        }

        empleado.setEsActivo(false);
        empleadoRepository.save(empleado);
    }

    private EmpleadoDTO convertirADTO(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();

        dto.setId(empleado.getId());
        dto.setLegajo(empleado.getLegajo());
        dto.setDni(empleado.getDni());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setFechaNacimiento(empleado.getFechaNacimiento());
        dto.setEmailCorporativo(empleado.getEmailCorporativo());
        dto.setCelularCorporativo(empleado.getCelularCorporativo());

        dto.setIdArea(empleado.getArea().getId());
        dto.setNombreArea(empleado.getArea().getNombre());

        dto.setFechaIngreso(empleado.getFechaIngreso());
        dto.setFechaBaja(empleado.getFechaBaja());
        dto.setTipoContrato(empleado.getTipoContrato());
        dto.setEsActivo(empleado.getEsActivo());
        dto.setEsResponsableArea(empleado.getEsResponsableArea());
        dto.setObservaciones(empleado.getObservaciones());

        // Campos calculados
        dto.setNombreCompleto(empleado.getNombreCompleto());
        dto.setCantidadActivosAsignados(
                asignacionRepository.countAsignacionesActivasByEmpleadoId(empleado.getId()).intValue()
        );

        return dto;
    }

    private void mapearDTOAEntidad(EmpleadoDTO dto, Empleado empleado) {
        empleado.setLegajo(dto.getLegajo());
        empleado.setDni(dto.getDni());
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setFechaNacimiento(dto.getFechaNacimiento());
        empleado.setEmailCorporativo(dto.getEmailCorporativo());
        empleado.setCelularCorporativo(dto.getCelularCorporativo());
        empleado.setFechaIngreso(dto.getFechaIngreso());
        empleado.setFechaBaja(dto.getFechaBaja());
        empleado.setTipoContrato(dto.getTipoContrato());
        empleado.setEsResponsableArea(dto.getEsResponsableArea());
        empleado.setObservaciones(dto.getObservaciones());

        // Obtener área
        Area area = areaRepository.findById(dto.getIdArea())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));
        empleado.setArea(area);
    }
}