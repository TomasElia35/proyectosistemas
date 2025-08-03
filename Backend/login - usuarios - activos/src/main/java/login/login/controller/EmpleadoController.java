package login.login.controller;

import login.login.DTO.EmpleadoDTO;
import login.login.DTO.ResponseDTO;
import login.login.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
@Validated
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EmpleadoDTO>>> listarTodos() {
        try {
            List<EmpleadoDTO> empleados = empleadoService.listarTodos();
            return ResponseEntity.ok(ResponseDTO.exito("Empleados obtenidos exitosamente", empleados));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener los empleados: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EmpleadoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return empleadoService.obtenerPorId(id)
                    .map(empleado -> ResponseEntity.ok(ResponseDTO.exito("Empleado encontrado", empleado)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Empleado no encontrado", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el empleado: " + e.getMessage()));
        }
    }

    @GetMapping("/legajo/{legajo}")
    public ResponseEntity<ResponseDTO<EmpleadoDTO>> obtenerPorLegajo(@PathVariable String legajo) {
        try {
            return empleadoService.obtenerPorLegajo(legajo)
                    .map(empleado -> ResponseEntity.ok(ResponseDTO.exito("Empleado encontrado", empleado)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Empleado no encontrado", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el empleado: " + e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<ResponseDTO<List<EmpleadoDTO>>> buscarEmpleados(@RequestParam String q) {
        try {
            List<EmpleadoDTO> empleados = empleadoService.buscarEmpleados(q);
            return ResponseEntity.ok(ResponseDTO.exito("Búsqueda realizada exitosamente", empleados));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error en la búsqueda: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<EmpleadoDTO>> crear(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        try {
            EmpleadoDTO empleadoCreado = empleadoService.crear(empleadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Empleado creado exitosamente", empleadoCreado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al crear el empleado: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<EmpleadoDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody EmpleadoDTO empleadoDTO) {
        try {
            EmpleadoDTO empleadoActualizado = empleadoService.actualizar(id, empleadoDTO);
            return ResponseEntity.ok(ResponseDTO.exito("Empleado actualizado exitosamente", empleadoActualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al actualizar el empleado: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable Long id) {
        try {
            empleadoService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.exito("Empleado eliminado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al eliminar el empleado: " + e.getMessage()));
        }
    }
}
