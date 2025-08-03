package login.login.controller;

import login.login.DTO.EstadoDTO;
import login.login.DTO.ResponseDTO;
import login.login.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/estados")
@CrossOrigin(origins = "*")
@Validated
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EstadoDTO>>> listarTodos() {
        try {
            List<EstadoDTO> estados = estadoService.listarTodos();
            return ResponseEntity.ok(ResponseDTO.exito("Estados obtenidos exitosamente", estados));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener los estados: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EstadoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return estadoService.obtenerPorId(id)
                    .map(estado -> ResponseEntity.ok(ResponseDTO.exito("Estado encontrado", estado)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Estado no encontrado", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el estado: " + e.getMessage()));
        }
    }

    @GetMapping("/asignables")
    public ResponseEntity<ResponseDTO<List<EstadoDTO>>> obtenerQuePermitenAsignacion() {
        try {
            List<EstadoDTO> estados = estadoService.obtenerQuePermitenAsignacion();
            return ResponseEntity.ok(ResponseDTO.exito("Estados que permiten asignación obtenidos", estados));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener estados asignables: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<EstadoDTO>> crear(@Valid @RequestBody EstadoDTO estadoDTO) {
        try {
            EstadoDTO estadoCreado = estadoService.crear(estadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Estado creado exitosamente", estadoCreado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al crear el estado: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<EstadoDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody EstadoDTO estadoDTO) {
        try {
            EstadoDTO estadoActualizado = estadoService.actualizar(id, estadoDTO);
            return ResponseEntity.ok(ResponseDTO.exito("Estado actualizado exitosamente", estadoActualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al actualizar el estado: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable Long id) {
        try {
            estadoService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.exito("Estado eliminado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al eliminar el estado: " + e.getMessage()));
        }
    }
}