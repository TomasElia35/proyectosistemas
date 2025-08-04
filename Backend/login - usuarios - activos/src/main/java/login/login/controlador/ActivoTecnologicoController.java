package login.login.controlador;

import login.login.DTO.*;
import login.login.servicio.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activos")
@RequiredArgsConstructor
public class ActivoTecnologicoController {

    private final ActivoTecnologicoService service;

    // ✅ Crear nuevo activo
    @PostMapping
    public ResponseEntity<ActivoTecnologicoResponseDTO> crear(@Valid @RequestBody ActivoTecnologicoRequestDTO dto) {
        return ResponseEntity.ok(service.crearActivo(dto));
    }

    // ✅ Listar todos los activos
    @GetMapping
    public ResponseEntity<List<ActivoTecnologicoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarActivos());
    }

    // ✅ Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<ActivoTecnologicoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // ✅ Actualizar activo
    @PutMapping("/{id}")
    public ResponseEntity<ActivoTecnologicoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ActivoTecnologicoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // ✅ Eliminación lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
