package login.login.controller;

import login.login.DTO.AreaDTO;
import login.login.DTO.ResponseDTO;
import login.login.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/areas")
@CrossOrigin(origins = "*")
@Validated
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AreaDTO>>> listarTodas() {
        try {
            List<AreaDTO> areas = areaService.listarTodas();
            return ResponseEntity.ok(ResponseDTO.exito("Áreas obtenidas exitosamente", areas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener las áreas: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AreaDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return areaService.obtenerPorId(id)
                    .map(area -> ResponseEntity.ok(ResponseDTO.exito("Área encontrada", area)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Área no encontrada", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el área: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<AreaDTO>> crear(@Valid @RequestBody AreaDTO areaDTO) {
        try {
            AreaDTO areaCreada = areaService.crear(areaDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Área creada exitosamente", areaCreada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al crear el área: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<AreaDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody AreaDTO areaDTO) {
        try {
            AreaDTO areaActualizada = areaService.actualizar(id, areaDTO);
            return ResponseEntity.ok(ResponseDTO.exito("Área actualizada exitosamente", areaActualizada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al actualizar el área: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable Long id) {
        try {
            areaService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.exito("Área eliminada exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al eliminar el área: " + e.getMessage()));
        }
    }
}