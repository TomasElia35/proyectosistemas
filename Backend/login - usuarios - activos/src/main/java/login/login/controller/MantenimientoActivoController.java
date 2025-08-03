package login.login.controller;

import login.login.DTO.*;
import login.login.service.MantenimientoActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
@CrossOrigin(origins = "*")
@Validated
public class MantenimientoActivoController {

    @Autowired
    private MantenimientoActivoService mantenimientoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MantenimientoActivoDTO>>> listarTodos() {
        try {
            List<MantenimientoActivoDTO> mantenimientos = mantenimientoService.listarTodos();
            return ResponseEntity.ok(ResponseDTO.exito("Mantenimientos obtenidos exitosamente", mantenimientos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener los mantenimientos: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MantenimientoActivoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return mantenimientoService.obtenerPorId(id)
                    .map(mantenimiento -> ResponseEntity.ok(ResponseDTO.exito("Mantenimiento encontrado", mantenimiento)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Mantenimiento no encontrado", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el mantenimiento: " + e.getMessage()));
        }
    }

    @GetMapping("/activo/{activoId}")
    public ResponseEntity<ResponseDTO<List<MantenimientoActivoDTO>>> obtenerPorActivo(@PathVariable Long activoId) {
        try {
            List<MantenimientoActivoDTO> mantenimientos = mantenimientoService.obtenerPorActivo(activoId);
            return ResponseEntity.ok(ResponseDTO.exito("Mantenimientos del activo obtenidos", mantenimientos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener mantenimientos del activo: " + e.getMessage()));
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<ResponseDTO<List<MantenimientoActivoDTO>>> obtenerPendientes() {
        try {
            List<MantenimientoActivoDTO> mantenimientos = mantenimientoService.obtenerPendientes();
            return ResponseEntity.ok(ResponseDTO.exito("Mantenimientos pendientes obtenidos", mantenimientos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener mantenimientos pendientes: " + e.getMessage()));
        }
    }

    @GetMapping("/atrasados")
    public ResponseEntity<ResponseDTO<List<MantenimientoActivoDTO>>> obtenerAtrasados() {
        try {
            List<MantenimientoActivoDTO> mantenimientos = mantenimientoService.obtenerAtrasados();
            return ResponseEntity.ok(ResponseDTO.exito("Mantenimientos atrasados obtenidos", mantenimientos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener mantenimientos atrasados: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<MantenimientoActivoDTO>> programarMantenimiento(@Valid @RequestBody CrearMantenimientoDTO crearDTO) {
        try {
            MantenimientoActivoDTO mantenimientoCreado = mantenimientoService.programarMantenimiento(crearDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Mantenimiento programado exitosamente", mantenimientoCreado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al programar el mantenimiento: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<ResponseDTO<MantenimientoActivoDTO>> completarMantenimiento(
            @PathVariable Long id,
            @Valid @RequestBody CompletarMantenimientoDTO completarDTO) {
        try {
            MantenimientoActivoDTO mantenimientoCompletado = mantenimientoService.completarMantenimiento(id, completarDTO);
            return ResponseEntity.ok(ResponseDTO.exito("Mantenimiento completado exitosamente", mantenimientoCompletado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al completar el mantenimiento: " + e.getMessage()));
        }
    }
}