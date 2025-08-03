package login.login.service;

import login.login.DTO.EstadoDTO;
import login.login.entidad.Estado;
import login.login.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<EstadoDTO> listarTodos() {
        return estadoRepository.findByEsActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<EstadoDTO> obtenerPorId(Long id) {
        return estadoRepository.findById(id)
                .filter(Estado::getEsActivo)
                .map(this::convertirADTO);
    }

    public List<EstadoDTO> obtenerQuePermitenAsignacion() {
        return estadoRepository.findByPermiteAsignacionTrueAndEsActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public EstadoDTO crear(EstadoDTO estadoDTO) {
        if (EstadoRepository.findByNombre(estadoDTO.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un estado con el nombre: " + estadoDTO.getNombre());
        }

        Estado estado = new Estado();
        estado.setNombre(estadoDTO.getNombre());
        estado.setDescripcion(estadoDTO.getDescripcion());
        estado.setPermiteAsignacion(estadoDTO.getPermiteAsignacion());
        estado.setRequiereAprobacion(estadoDTO.getRequiereAprobacion());
        estado.setEsEstadoFinal(estadoDTO.getEsEstadoFinal());

        Estado estadoGuardado = estadoRepository.save(estado);
        return convertirADTO(estadoGuardado);
    }

    public EstadoDTO actualizar(Long id, EstadoDTO estadoDTO) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        if (!estado.getEsActivo()) {
            throw new RuntimeException("No se puede actualizar un estado inactivo");
        }

        Optional<Estado> estadoExistente = EstadoRepository.findByNombre(estadoDTO.getNombre());
        if (estadoExistente.isPresent() && !estadoExistente.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe un estado con el nombre: " + estadoDTO.getNombre());
        }

        estado.setNombre(estadoDTO.getNombre());
        estado.setDescripcion(estadoDTO.getDescripcion());
        estado.setPermiteAsignacion(estadoDTO.getPermiteAsignacion());
        estado.setRequiereAprobacion(estadoDTO.getRequiereAprobacion());
        estado.setEsEstadoFinal(estadoDTO.getEsEstadoFinal());

        Estado estadoActualizado = estadoRepository.save(estado);
        return convertirADTO(estadoActualizado);
    }

    public void eliminar(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        estado.setEsActivo(false);
        estadoRepository.save(estado);
    }

    private EstadoDTO convertirADTO(Estado estado) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(estado.getId());
        dto.setNombre(estado.getNombre());
        dto.setDescripcion(estado.getDescripcion());
        dto.setPermiteAsignacion(estado.getPermiteAsignacion());
        dto.setRequiereAprobacion(estado.getRequiereAprobacion());
        dto.setEsEstadoFinal(estado.getEsEstadoFinal());
        dto.setEsActivo(estado.getEsActivo());
        return dto;
    }
}