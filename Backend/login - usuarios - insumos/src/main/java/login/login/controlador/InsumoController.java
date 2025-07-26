package login.login.controlador;

import login.login.DTO.*;
import login.login.servicio.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@CrossOrigin(origins = "*")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    /**
     * Crear un nuevo insumo
     * Requiere autenticación
     */
    @PostMapping
    public ResponseEntity<?> crearInsumo(@Valid @RequestBody InsumoRequestDTO insumoRequest) {
        try {
            InsumoResponseDTO insumo = insumoService.crearInsumo(insumoRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Insumo creado exitosamente", insumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Obtener todos los insumos activos
     * Requiere autenticación
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosInsumos() {
        try {
            List<InsumoResponseDTO> insumos = insumoService.obtenerTodosLosInsumos();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Insumos obtenidos exitosamente", insumos));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Obtener un insumo por ID
     * Requiere autenticación
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInsumoPorId(@PathVariable Integer id) {
        try {
            InsumoResponseDTO insumo = insumoService.obtenerInsumoPorId(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Insumo obtenido exitosamente", insumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Obtener un insumo por código
     * Requiere autenticación
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> obtenerInsumoPorCodigo(@PathVariable String codigo) {
        try {
            InsumoResponseDTO insumo = insumoService.obtenerInsumoPorCodigo(codigo);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Insumo obtenido exitosamente", insumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Actualizar un insumo
     * Requiere autenticación
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInsumo(@PathVariable Integer id,
                                              @Valid @RequestBody InsumoRequestDTO insumoRequest) {
        try {
            InsumoResponseDTO insumo = insumoService.actualizarInsumo(id, insumoRequest);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Insumo actualizado exitosamente", insumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Eliminar un insumo (eliminación lógica)
     * Solo administradores
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarInsumo(@PathVariable Integer id) {
        try {
            insumoService.eliminarInsumo(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Insumo eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Actualizar stock de un insumo
     * Requiere autenticación
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Integer id,
                                             @Valid @RequestBody InsumoUpdateStockDTO updateStockDTO) {
        try {
            InsumoResponseDTO insumo = insumoService.actualizarStock(id, updateStockDTO);
            String mensaje = "ENTRADA".equals(updateStockDTO.getTipoOperacion())
                    ? "Stock incrementado exitosamente"
                    : "Stock decrementado exitosamente";
            return ResponseEntity.ok(new ApiResponseDTO(true, mensaje, insumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Cambiar estado de un insumo (activar/desactivar)
     * Solo administradores
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> cambiarEstadoInsumo(@PathVariable Integer id,
                                                 @RequestParam Boolean estado) {
        try {
            insumoService.cambiarEstadoInsumo(id, estado);
            String mensaje = estado ? "Insumo activado exitosamente" : "Insumo desactivado exitosamente";
            return ResponseEntity.ok(new ApiResponseDTO(true, mensaje));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Buscar insumos con filtros
     * Requiere autenticación
     */
    @PostMapping("/buscar")
    public ResponseEntity<?> buscarInsumos(@RequestBody InsumoFiltroDTO filtro) {
        try {
            List<InsumoResponseDTO> insumos = insumoService.buscarInsumos(filtro);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Búsqueda realizada exitosamente", insumos));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Obtener insumos con stock bajo
     * Requiere autenticación
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<?> obtenerInsumosConStockBajo() {
        try {
            List<InsumoResponseDTO> insumos = insumoService.obtenerInsumosConStockBajo();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Insumos con stock bajo obtenidos exitosamente", insumos));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Obtener todas las categorías únicas
     * Requiere autenticación
     */
    @GetMapping("/categorias")
    public ResponseEntity<?> obtenerCategorias() {
        try {
            List<String> categorias = insumoService.obtenerCategorias();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Categorías obtenidas exitosamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    /**
     * Obtener todos los proveedores únicos
     * Requiere autenticación
     */
    @GetMapping("/proveedores")
    public ResponseEntity<?> obtenerProveedores() {
        try {
            List<String> proveedores = insumoService.obtenerProveedores();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Proveedores obtenidos exitosamente", proveedores));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }
}