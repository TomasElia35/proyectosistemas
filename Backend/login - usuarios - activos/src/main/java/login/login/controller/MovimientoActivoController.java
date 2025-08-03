package login.login.controller;

import login.login.DTO.*;
import login.login.entidad.MovimientoActivo;
import login.login.service.MovimientoActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
@Validated
public class MovimientoActivoController {

    @Autowired
    private MovimientoActivoService movimientoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MovimientoActivoDTO>>> listarTodos() {
        try {
            List<MovimientoActivoDTO> movimientos = movimientoService.listarTodos();
            return ResponseEntity.ok(ResponseDTO.exito("Movimientos obtenidos exitosamente", movimientos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener los movimientos: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MovimientoActivoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return movimientoService.obtenerPorId(id)
                    .map(movimiento -> ResponseEntity.ok(ResponseDTO.exito("Movimiento encontrado", movimiento)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Movimiento no encontrado", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el movimiento: " + e.getMessage()));
        }
    }

    @GetMapping("/activo/{activoId}")
    public ResponseEntity<ResponseDTO<List<MovimientoActivoDTO>>> obtenerPorActivo(@PathVariable Long activoId) {
        try {
            List<MovimientoActivoDTO> movimientos = movimientoService.obtenerPorActivo(activoId);
            return ResponseEntity.ok(ResponseDTO.exito("Movimientos del activo obtenidos", movimientos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener movimientos del activo: " + e.getMessage()));
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ResponseDTO<List<MovimientoActivoDTO>>> obtenerPorTipo(@PathVariable MovimientoActivo.TipoMovimiento tipo) {
        try {
            List<MovimientoActivoDTO> movimientos = movimientoService.obtenerPorTipo(tipo);
            return ResponseEntity.ok(ResponseDTO.exito("Movimientos por tipo obtenidos", movimientos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener movimientos por tipo: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<MovimientoActivoDTO>> registrarMovimiento(@Valid @RequestBody CrearMovimientoDTO crearDTO) {
        try {
            MovimientoActivoDTO movimientoCreado = movimientoService.registrarMovimiento(crearDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Movimiento registrado exitosamente", movimientoCreado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al registrar el movimiento: " + e.getMessage()));
        }
    }
}
