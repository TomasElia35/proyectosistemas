package login.login.controller;

import login.login.DTO.ProveedorDTO;
import login.login.DTO.ResponseDTO;
import login.login.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
@Validated
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ProveedorDTO>>> listarTodos() {
        try {
            List<ProveedorDTO> proveedores = proveedorService.listarTodos();
            return ResponseEntity.ok(ResponseDTO.exito("Proveedores obtenidos exitosamente", proveedores));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener los proveedores: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProveedorDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return proveedorService.obtenerPorId(id)
                    .map(proveedor -> ResponseEntity.ok(ResponseDTO.exito("Proveedor encontrado", proveedor)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Proveedor no encontrado", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el proveedor: " + e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<ResponseDTO<List<ProveedorDTO>>> buscarProveedores(@RequestParam String q) {
        try {
            List<ProveedorDTO> proveedores = proveedorService.buscarProveedores(q);
            return ResponseEntity.ok(ResponseDTO.exito("Búsqueda realizada exitosamente", proveedores));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error en la búsqueda: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ProveedorDTO>> crear(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        try {
            ProveedorDTO proveedorCreado = proveedorService.crear(proveedorDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Proveedor creado exitosamente", proveedorCreado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al crear el proveedor: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProveedorDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO proveedorDTO) {
        try {
            ProveedorDTO proveedorActualizado = proveedorService.actualizar(id, proveedorDTO);
            return ResponseEntity.ok(ResponseDTO.exito("Proveedor actualizado exitosamente", proveedorActualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al actualizar el proveedor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable Long id) {
        try {
            proveedorService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.exito("Proveedor eliminado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al eliminar el proveedor: " + e.getMessage()));
        }
    }
}