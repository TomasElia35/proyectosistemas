package login.login.controller;

import login.login.DTO.*;
import login.login.service.ActivoTecnologicoService;
import login.login.service.AsignacionActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/activos")
@CrossOrigin(origins = "*")
@Validated
public class ActivoTecnologicoController {

    @Autowired
    private ActivoTecnologicoService activoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ActivoTecnologicoDTO>>> listarTodos() {
        try {
            List<ActivoTecnologicoDTO> activos = activoService.listarTodos();
            return ResponseEntity.ok(ResponseDTO.exito("Activos obtenidos exitosamente", activos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener asignaciones del empleado: " + e.getMessage()));
        }
    }

    @GetMapping("/empleado/{empleadoId}/activas")
    public ResponseEntity<ResponseDTO<List<AsignacionActivoDTO>>> obtenerActivasPorEmpleado(@PathVariable Long empleadoId) {
        try {
            List<AsignacionActivoDTO> asignaciones = AsignacionActivoService.obtenerActivasPorEmpleado(empleadoId);
            return ResponseEntity.ok(ResponseDTO.exito("Asignaciones activas del empleado obtenidas", asignaciones));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener asignaciones activas del empleado: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<AsignacionActivoDTO>> asignarActivo(@Valid @RequestBody CrearAsignacionDTO crearDTO) {
        try {
            AsignacionActivoDTO asignacionCreada = AsignacionActivoService.asignarActivo(crearDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Activo asignado exitosamente", asignacionCreada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al asignar el activo: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/desasignar")
    public ResponseEntity<ResponseDTO<Void>> desasignarActivo(
            @PathVariable Long id,
            @Valid @RequestBody DesasignarActivoDTO desasignarDTO) {
        try {
            AsignacionActivoService.desasignarActivo(id, desasignarDTO);
            return ResponseEntity.ok(ResponseDTO.exito("Activo desasignado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al desasignar el activo: " + e.getMessage()));
        }
    }
}