package login.login.controller;

import login.login.DTO.*;
import login.login.service.AsignacionActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "*")
@Validated
public class AsignacionActivoController {

    @Autowired
    private AsignacionActivoService asignacionService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AsignacionActivoDTO>>> listarTodas() {
        try {
            List<AsignacionActivoDTO> asignaciones = asignacionService.listarTodas();
            return ResponseEntity.ok(ResponseDTO.exito("Asignaciones obtenidas exitosamente", asignaciones));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener las asignaciones: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AsignacionActivoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return asignacionService.obtenerPorId(id)
                    .map(asignacion -> ResponseEntity.ok(ResponseDTO.exito("Asignación encontrada", asignacion)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Asignación no encontrada", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener la asignación: " + e.getMessage()));
        }
    }

    @GetMapping("/activas")
    public ResponseEntity<ResponseDTO<List<AsignacionActivoDTO>>> obtenerActivas() {
        try {
            List<AsignacionActivoDTO> asignaciones = asignacionService.obtenerAsignacionesActivas();
            return ResponseEntity.ok(ResponseDTO.exito("Asignaciones activas obtenidas", asignaciones));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener asignaciones activas: " + e.getMessage()));
        }
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<ResponseDTO<List<AsignacionActivoDTO>>> obtenerPorEmpleado(@PathVariable Long empleadoId) {
        try {
            List<AsignacionActivoDTO> asignaciones = asignacionService.obtenerPorEmpleado(empleadoId);
            return ResponseEntity.ok(ResponseDTO.exito("Asignaciones del empleado obtenidas", asignaciones));
        } catch (Exception e) {
            return (ResponseEntity<ResponseDTO<List<AsignacionActivoDTO>>>) (ResponseEntity<ResponseDTO<List<AsignacionActivoDTO>>>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}