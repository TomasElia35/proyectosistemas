package login.login.servicio;


import login.login.DTO.*;
import login.login.modelo.*;
import login.login.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivoTecnologicoService {

    private final ActivoTecnologicoRepository activoRepo;
    private final ModeloArticuloRepository modeloArticuloRepo;
    private final ProveedorRepository proveedorRepo;
    private final EstadoRepository estadoRepo;
    private final EmpleadoRepository empleadoRepo;

    @Transactional
    public ActivoTecnologicoResponseDTO crearActivo(ActivoTecnologicoRequestDTO dto) {
        ActivoTecnologico activo = mapToEntity(dto);
        activo = activoRepo.save(activo);
        return mapToResponseDTO(activo);
    }

    public List<ActivoTecnologicoResponseDTO> listarActivos() {
        return activoRepo.findByActivoTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ActivoTecnologicoResponseDTO obtenerPorId(Long id) {
        return activoRepo.findById(id)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));
    }

    @Transactional
    public ActivoTecnologicoResponseDTO actualizar(Long id, ActivoTecnologicoRequestDTO dto) {
        ActivoTecnologico existente = activoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));

        ActivoTecnologico actualizado = mapToEntity(dto);
        actualizado.setId(existente.getId());
        actualizado.setFechaCreacion(existente.getFechaCreacion());

        activoRepo.save(actualizado);
        return mapToResponseDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        ActivoTecnologico activo = activoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));
        activo.setActivo(false);
        activoRepo.save(activo);
    }

    // ---------- Mapeos --------------------

    private ActivoTecnologico mapToEntity(ActivoTecnologicoRequestDTO dto) {
        return ActivoTecnologico.builder()
                .codigoInventario(dto.getCodigoInventario())
                .codigoTecnico(dto.getCodigoTecnico())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .modeloArticulo(modeloArticuloRepo.findById(dto.getIdModeloArticulo())
                        .orElseThrow(() -> new RuntimeException("Modelo no encontrado")))
                .proveedor(proveedorRepo.findById(dto.getIdProveedor())
                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado")))
                .costo(dto.getCosto())
                .fechaAdquisicion(dto.getFechaAdquisicion())
                .garantiaMeses(dto.getGarantiaMeses())
                .fechaVencimientoGarantia(dto.getFechaVencimientoGarantia())
                .estado(estadoRepo.findById(dto.getIdEstado())
                        .orElseThrow(() -> new RuntimeException("Estado no encontrado")))
                .empleado(empleadoRepo.findById(dto.getIdEmpleado())
                        .orElseThrow(() -> new RuntimeException("Empleado no encontrado")))
                .observaciones(dto.getObservaciones())
                .usuarioCreacion(dto.getUsuarioCreacion())
                .usuarioModificacion(dto.getUsuarioModificacion())
                .activo(dto.getActivo())
                .build();
    }

    private ActivoTecnologicoResponseDTO mapToResponseDTO(ActivoTecnologico a) {
        Empleado e = a.getEmpleado();
        Area area = e.getArea();

        return ActivoTecnologicoResponseDTO.builder()
                .id(a.getId())
                .codigoInventario(a.getCodigoInventario())
                .codigoTecnico(a.getCodigoTecnico())
                .nombre(a.getNombre())
                .descripcion(a.getDescripcion())
                .idModeloArticulo(a.getModeloArticulo().getId())
                .modeloArticuloMarca(a.getModeloArticulo().getMarca())
                .modeloArticuloModelo(a.getModeloArticulo().getModelo())
                .idProveedor(a.getProveedor().getId())
                .proveedorNombre(a.getProveedor().getNombre())
                .costo(a.getCosto())
                .fechaAdquisicion(a.getFechaAdquisicion())
                .garantiaMeses(a.getGarantiaMeses())
                .fechaVencimientoGarantia(a.getFechaVencimientoGarantia())
                .idEstado(a.getEstado().getId())
                .estadoNombre(a.getEstado().getNombre())
                .idEmpleado(e.getId())
                .empleadoNombreCompleto(e.getNombre() + " " + e.getApellido())
                .idArea(area.getId())
                .areaNombre(area.getNombre())
                .observaciones(a.getObservaciones())
                .usuarioCreacion(a.getUsuarioCreacion())
                .usuarioModificacion(a.getUsuarioModificacion())
                .activo(a.getActivo())
                .fechaCreacion(a.getFechaCreacion())
                .fechaUltimaModificacion(a.getFechaUltimaModificacion())
                .build();
    }
}
