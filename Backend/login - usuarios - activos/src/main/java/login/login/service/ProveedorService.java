package login.login.service;

import login.login.DTO.ProveedorDTO;
import login.login.entidad.Proveedor;
import login.login.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<ProveedorDTO> listarTodos() {
        return proveedorRepository.findByEsActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<ProveedorDTO> obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .filter(Proveedor::getEsActivo)
                .map(this::convertirADTO);
    }

    public List<ProveedorDTO> buscarProveedores(String busqueda) {
        return proveedorRepository.buscarProveedores(busqueda)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProveedorDTO crear(ProveedorDTO proveedorDTO) {
        if (proveedorRepository.findByNombre(proveedorDTO.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un proveedor con el nombre: " + proveedorDTO.getNombre());
        }

        if (proveedorRepository.findByCuit(proveedorDTO.getCuit()).isPresent()) {
            throw new RuntimeException("Ya existe un proveedor con el CUIT: " + proveedorDTO.getCuit());
        }

        Proveedor proveedor = new Proveedor();
        mapearDTOAEntidad(proveedorDTO, proveedor);

        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return convertirADTO(proveedorGuardado);
    }

    public ProveedorDTO actualizar(Long id, ProveedorDTO proveedorDTO) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        if (!proveedor.getEsActivo()) {
            throw new RuntimeException("No se puede actualizar un proveedor inactivo");
        }

        // Validar unicidad de nombre y CUIT
        Optional<Proveedor> proveedorPorNombre = proveedorRepository.findByNombre(proveedorDTO.getNombre());
        if (proveedorPorNombre.isPresent() && !proveedorPorNombre.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe un proveedor con el nombre: " + proveedorDTO.getNombre());
        }

        Optional<Proveedor> proveedorPorCuit = proveedorRepository.findByCuit(proveedorDTO.getCuit());
        if (proveedorPorCuit.isPresent() && !proveedorPorCuit.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe un proveedor con el CUIT: " + proveedorDTO.getCuit());
        }

        mapearDTOAEntidad(proveedorDTO, proveedor);

        Proveedor proveedorActualizado = proveedorRepository.save(proveedor);
        return convertirADTO(proveedorActualizado);
    }

    public void eliminar(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedor.setEsActivo(false);
        proveedorRepository.save(proveedor);
    }

    private ProveedorDTO convertirADTO(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setCuit(proveedor.getCuit());
        dto.setEmailContacto(proveedor.getEmailContacto());
        dto.setTelefonoContacto(proveedor.getTelefonoContacto());
        dto.setNombreContactoPrincipal(proveedor.getNombreContactoPrincipal());
        dto.setDireccion(proveedor.getDireccion());
        dto.setFormaPago(proveedor.getFormaPago());
        dto.setMonedaOperacion(proveedor.getMonedaOperacion());
        dto.setDiasPagoPromedio(proveedor.getDiasPagoPromedio());
        dto.setEsActivo(proveedor.getEsActivo());
        dto.setObservaciones(proveedor.getObservaciones());

        // Calcular cantidades relacionadas
        if (proveedor.getActivos() != null) {
            dto.setCantidadActivosProvistos((int) proveedor.getActivos().stream()
                    .filter(activo -> activo.getEsActivo())
                    .count());
        }

        if (proveedor.getMantenimientos() != null) {
            dto.setCantidadMantenimientosRealizados(proveedor.getMantenimientos().size());
        }

        return dto;
    }

    private void mapearDTOAEntidad(ProveedorDTO dto, Proveedor proveedor) {
        proveedor.setNombre(dto.getNombre());
        proveedor.setCuit(dto.getCuit());
        proveedor.setEmailContacto(dto.getEmailContacto());
        proveedor.setTelefonoContacto(dto.getTelefonoContacto());
        proveedor.setNombreContactoPrincipal(dto.getNombreContactoPrincipal());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setFormaPago(dto.getFormaPago());
        proveedor.setMonedaOperacion(dto.getMonedaOperacion());
        proveedor.setDiasPagoPromedio(dto.getDiasPagoPromedio());
        proveedor.setObservaciones(dto.getObservaciones());
    }
}