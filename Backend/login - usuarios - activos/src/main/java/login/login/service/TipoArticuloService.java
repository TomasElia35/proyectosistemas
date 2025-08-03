package login.login.service;

import login.login.DTO.TipoArticuloDTO;
import login.login.entidad.TipoArticulo;
import login.login.repository.TipoArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoArticuloService {

    @Autowired
    private TipoArticuloRepository tipoArticuloRepository;

    public List<TipoArticuloDTO> listarTodos() {
        return tipoArticuloRepository.findByEsActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<TipoArticuloDTO> obtenerPorId(Long id) {
        return tipoArticuloRepository.findById(id)
                .filter(TipoArticulo::getEsActivo)
                .map(this::convertirADTO);
    }

    public List<TipoArticuloDTO> obtenerPorCategoria(TipoArticulo.CategoriaArticulo categoria) {
        return tipoArticuloRepository.findByCategoria(categoria)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public TipoArticuloDTO crear(TipoArticuloDTO tipoArticuloDTO) {
        if (tipoArticuloRepository.findByNombre(tipoArticuloDTO.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un tipo de artículo con el nombre: " + tipoArticuloDTO.getNombre());
        }

        TipoArticulo tipoArticulo = new TipoArticulo();
        tipoArticulo.setNombre(tipoArticuloDTO.getNombre());
        tipoArticulo.setDescripcion(tipoArticuloDTO.getDescripcion());
        tipoArticulo.setCategoria(tipoArticuloDTO.getCategoria());
        tipoArticulo.setRequiereNumeroSerie(tipoArticuloDTO.getRequiereNumeroSerie());
        tipoArticulo.setVidaUtilDefectoAnios(tipoArticuloDTO.getVidaUtilDefectoAnios());
        tipoArticulo.setRequiereMantenimiento(tipoArticuloDTO.getRequiereMantenimiento());

        TipoArticulo tipoArticuloGuardado = tipoArticuloRepository.save(tipoArticulo);
        return convertirADTO(tipoArticuloGuardado);
    }

    public TipoArticuloDTO actualizar(Long id, TipoArticuloDTO tipoArticuloDTO) {
        TipoArticulo tipoArticulo = tipoArticuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de artículo no encontrado"));

        if (!tipoArticulo.getEsActivo()) {
            throw new RuntimeException("No se puede actualizar un tipo de artículo inactivo");
        }

        Optional<TipoArticulo> tipoExistente = tipoArticuloRepository.findByNombre(tipoArticuloDTO.getNombre());
        if (tipoExistente.isPresent() && !tipoExistente.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe un tipo de artículo con el nombre: " + tipoArticuloDTO.getNombre());
        }

        tipoArticulo.setNombre(tipoArticuloDTO.getNombre());
        tipoArticulo.setDescripcion(tipoArticuloDTO.getDescripcion());
        tipoArticulo.setCategoria(tipoArticuloDTO.getCategoria());
        tipoArticulo.setRequiereNumeroSerie(tipoArticuloDTO.getRequiereNumeroSerie());
        tipoArticulo.setVidaUtilDefectoAnios(tipoArticuloDTO.getVidaUtilDefectoAnios());
        tipoArticulo.setRequiereMantenimiento(tipoArticuloDTO.getRequiereMantenimiento());

        TipoArticulo tipoArticuloActualizado = tipoArticuloRepository.save(tipoArticulo);
        return convertirADTO(tipoArticuloActualizado);
    }

    public void eliminar(Long id) {
        TipoArticulo tipoArticulo = tipoArticuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de artículo no encontrado"));

        tipoArticulo.setEsActivo(false);
        tipoArticuloRepository.save(tipoArticulo);
    }

    private TipoArticuloDTO convertirADTO(TipoArticulo tipoArticulo) {
        TipoArticuloDTO dto = new TipoArticuloDTO();
        dto.setId(tipoArticulo.getId());
        dto.setNombre(tipoArticulo.getNombre());
        dto.setDescripcion(tipoArticulo.getDescripcion());
        dto.setCategoria(tipoArticulo.getCategoria());
        dto.setRequiereNumeroSerie(tipoArticulo.getRequiereNumeroSerie());
        dto.setVidaUtilDefectoAnios(tipoArticulo.getVidaUtilDefectoAnios());
        dto.setRequiereMantenimiento(tipoArticulo.getRequiereMantenimiento());
        dto.setEsActivo(tipoArticulo.getEsActivo());
        return dto;
    }
}