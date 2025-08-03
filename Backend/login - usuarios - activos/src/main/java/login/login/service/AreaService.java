package login.login.service;

import login.login.DTO.AreaDTO;
import login.login.entidad.Area;
import login.login.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public List<AreaDTO> listarTodas() {
        return areaRepository.findByEsActivaTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<AreaDTO> obtenerPorId(Long id) {
        return areaRepository.findById(id)
                .filter(Area::getEsActiva)
                .map(this::convertirADTO);
    }

    public AreaDTO crear(AreaDTO areaDTO) {
        if (areaRepository.findByNombre(areaDTO.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un área con el nombre: " + areaDTO.getNombre());
        }

        Area area = new Area();
        area.setNombre(areaDTO.getNombre());
        area.setDescripcion(areaDTO.getDescripcion());
        area.setCentroCosto(areaDTO.getCentroCosto());
        area.setIdResponsable(areaDTO.getIdResponsable());
        area.setUbicacionFisica(areaDTO.getUbicacionFisica());

        Area areaGuardada = areaRepository.save(area);
        return convertirADTO(areaGuardada);
    }

    public AreaDTO actualizar(Long id, AreaDTO areaDTO) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        if (!area.getEsActiva()) {
            throw new RuntimeException("No se puede actualizar un área inactiva");
        }

        // Verificar que no exista otro área con el mismo nombre
        Optional<Area> areaExistente = areaRepository.findByNombre(areaDTO.getNombre());
        if (areaExistente.isPresent() && !areaExistente.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe un área con el nombre: " + areaDTO.getNombre());
        }

        area.setNombre(areaDTO.getNombre());
        area.setDescripcion(areaDTO.getDescripcion());
        area.setCentroCosto(areaDTO.getCentroCosto());
        area.setIdResponsable(areaDTO.getIdResponsable());
        area.setUbicacionFisica(areaDTO.getUbicacionFisica());

        Area areaActualizada = areaRepository.save(area);
        return convertirADTO(areaActualizada);
    }

    public void eliminar(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        area.setEsActiva(false);
        areaRepository.save(area);
    }

    private AreaDTO convertirADTO(Area area) {
        AreaDTO dto = new AreaDTO();
        dto.setId(area.getId());
        dto.setNombre(area.getNombre());
        dto.setDescripcion(area.getDescripcion());
        dto.setCentroCosto(area.getCentroCosto());
        dto.setIdResponsable(area.getIdResponsable());
        dto.setUbicacionFisica(area.getUbicacionFisica());
        dto.setEsActiva(area.getEsActiva());

        // Calcular cantidades relacionadas
        if (area.getEmpleados() != null) {
            dto.setCantidadEmpleados((int) area.getEmpleados().stream()
                    .filter(empleado -> empleado.getEsActivo())
                    .count());
        }

        if (area.getActivos() != null) {
            dto.setCantidadActivos((int) area.getActivos().stream()
                    .filter(activo -> activo.getEsActivo())
                    .count());
        }

        return dto;
    }
}