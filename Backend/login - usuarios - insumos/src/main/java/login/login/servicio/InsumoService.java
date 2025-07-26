package login.login.servicio;

import login.login.DTO.*;
import login.login.modelo.Insumo;
import login.login.modelo.Usuario;
import login.login.repositorio.InsumoRepository;
import login.login.repositorio.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public InsumoResponseDTO crearInsumo(InsumoRequestDTO insumoRequest) {
        // Verificar si el código ya existe
        if (insumoRepository.existsByCodigo(insumoRequest.getCodigo())) {
            throw new RuntimeException("Ya existe un insumo con el código: " + insumoRequest.getCodigo());
        }

        // Obtener usuario autenticado
        Usuario usuarioCreador = obtenerUsuarioAutenticado();

        // Crear el insumo
        Insumo insumo = new Insumo();
        insumo.setNombre(insumoRequest.getNombre());
        insumo.setDescripcion(insumoRequest.getDescripcion());
        insumo.setCodigo(insumoRequest.getCodigo().toUpperCase()); // Normalizar código
        insumo.setPrecio(insumoRequest.getPrecio());
        insumo.setStock(insumoRequest.getStock());
        insumo.setStockMinimo(insumoRequest.getStockMinimo());
        insumo.setUnidadMedida(insumoRequest.getUnidadMedida());
        insumo.setCategoria(insumoRequest.getCategoria());
        insumo.setProveedor(insumoRequest.getProveedor());
        insumo.setEstado(insumoRequest.getEstado());
        insumo.setUsuarioCreador(usuarioCreador);

        insumo = insumoRepository.save(insumo);

        return convertirAInsumoResponseDTO(insumo);
    }

    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> obtenerTodosLosInsumos() {
        return insumoRepository.findAllActivosOrderByNombre().stream()
                .map(this::convertirAInsumoResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InsumoResponseDTO obtenerInsumoPorId(Integer id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));

        return convertirAInsumoResponseDTO(insumo);
    }

    @Transactional(readOnly = true)
    public InsumoResponseDTO obtenerInsumoPorCodigo(String codigo) {
        Insumo insumo = insumoRepository.findByCodigo(codigo.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con código: " + codigo));

        return convertirAInsumoResponseDTO(insumo);
    }

    public InsumoResponseDTO actualizarInsumo(Integer id, InsumoRequestDTO insumoRequest) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));

        // Verificar si el código ya existe y no es del mismo insumo
        String codigoNormalizado = insumoRequest.getCodigo().toUpperCase();
        if (insumoRepository.existsByCodigo(codigoNormalizado) &&
                !insumo.getCodigo().equals(codigoNormalizado)) {
            throw new RuntimeException("Ya existe un insumo con el código: " + codigoNormalizado);
        }

        // Actualizar campos
        insumo.setNombre(insumoRequest.getNombre());
        insumo.setDescripcion(insumoRequest.getDescripcion());
        insumo.setCodigo(codigoNormalizado);
        insumo.setPrecio(insumoRequest.getPrecio());
        insumo.setStock(insumoRequest.getStock());
        insumo.setStockMinimo(insumoRequest.getStockMinimo());
        insumo.setUnidadMedida(insumoRequest.getUnidadMedida());
        insumo.setCategoria(insumoRequest.getCategoria());
        insumo.setProveedor(insumoRequest.getProveedor());
        insumo.setEstado(insumoRequest.getEstado());

        insumo = insumoRepository.save(insumo);

        return convertirAInsumoResponseDTO(insumo);
    }

    public void eliminarInsumo(Integer id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));

        // Eliminación lógica
        insumo.setEstado(false);
        insumoRepository.save(insumo);
    }

    public InsumoResponseDTO actualizarStock(Integer id, InsumoUpdateStockDTO updateStockDTO) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));

        if (!insumo.getEstado()) {
            throw new RuntimeException("No se puede actualizar el stock de un insumo inactivo");
        }

        int nuevoStock;
        if ("ENTRADA".equals(updateStockDTO.getTipoOperacion())) {
            nuevoStock = insumo.getStock() + updateStockDTO.getCantidad();
        } else if ("SALIDA".equals(updateStockDTO.getTipoOperacion())) {
            nuevoStock = insumo.getStock() - updateStockDTO.getCantidad();
            if (nuevoStock < 0) {
                throw new RuntimeException("Stock insuficiente. Stock actual: " + insumo.getStock() +
                        ", Cantidad solicitada: " + updateStockDTO.getCantidad());
            }
        } else {
            throw new RuntimeException("Tipo de operación inválido: " + updateStockDTO.getTipoOperacion());
        }

        insumo.setStock(nuevoStock);
        insumo = insumoRepository.save(insumo);

        return convertirAInsumoResponseDTO(insumo);
    }

    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> buscarInsumos(InsumoFiltroDTO filtro) {
        List<Insumo> insumos;

        if (filtro.getSoloStockBajo() != null && filtro.getSoloStockBajo()) {
            insumos = insumoRepository.findInsumosConStockBajo();
        } else {
            insumos = insumoRepository.findAllActivosOrderByNombre();
        }

        // Aplicar filtros
        return insumos.stream()
                .filter(insumo -> filtro.getNombre() == null ||
                        insumo.getNombre().toLowerCase().contains(filtro.getNombre().toLowerCase()))
                .filter(insumo -> filtro.getCategoria() == null ||
                        filtro.getCategoria().equals(insumo.getCategoria()))
                .filter(insumo -> filtro.getProveedor() == null ||
                        filtro.getProveedor().equals(insumo.getProveedor()))
                .filter(insumo -> filtro.getEstado() == null ||
                        filtro.getEstado().equals(insumo.getEstado()))
                .filter(insumo -> filtro.getStockMinimo() == null ||
                        insumo.getStock() >= filtro.getStockMinimo())
                .filter(insumo -> filtro.getStockMaximo() == null ||
                        insumo.getStock() <= filtro.getStockMaximo())
                .map(this::convertirAInsumoResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> obtenerInsumosConStockBajo() {
        return insumoRepository.findInsumosConStockBajo().stream()
                .map(this::convertirAInsumoResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> obtenerCategorias() {
        return insumoRepository.findDistinctCategorias();
    }

    @Transactional(readOnly = true)
    public List<String> obtenerProveedores() {
        return insumoRepository.findDistinctProveedores();
    }

    public void cambiarEstadoInsumo(Integer id, Boolean estado) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));

        insumo.setEstado(estado);
        insumoRepository.save(insumo);
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return usuarioRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
    }

    private InsumoResponseDTO convertirAInsumoResponseDTO(Insumo insumo) {
        InsumoResponseDTO dto = modelMapper.map(insumo, InsumoResponseDTO.class);

        // Verificar si el stock está bajo
        dto.setStockBajo(insumo.isStockBajo());

        // Mapear usuario creador si existe
        if (insumo.getUsuarioCreador() != null) {
            InsumoResponseDTO.UsuarioSimpleDTO usuarioDTO = new InsumoResponseDTO.UsuarioSimpleDTO();
            usuarioDTO.setId(insumo.getUsuarioCreador().getId());
            usuarioDTO.setNombre(insumo.getUsuarioCreador().getNombre());
            usuarioDTO.setApellido(insumo.getUsuarioCreador().getApellido());
            usuarioDTO.setMail(insumo.getUsuarioCreador().getMail());
            dto.setUsuarioCreador(usuarioDTO);
        }

        return dto;
    }
}