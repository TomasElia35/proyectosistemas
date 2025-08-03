package login.login.controller;

import login.login.DTO.ResponseDTO;
import login.login.DTO.TipoArticuloDTO;
import login.login.entidad.TipoArticulo;
import login.login.service.TipoArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-articulo")
@CrossOrigin(origins = "*")
@Validated
public class TipoArticuloController {

    @Autowired
    private TipoArticuloService tipoArticuloService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<TipoArticuloDTO>>> listarTodos() {
        try {
            List<TipoArticuloDTO> tiposArticulo = tipoArticuloService.listarTodos();
            return ResponseEntity.ok(ResponseDTO.exito("Tipos de artículo obtenidos exitosamente", tiposArticulo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener los tipos de artículo: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<TipoArticuloDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            return tipoArticuloService.obtenerPorId(id)
                    .map(tipoArticulo -> ResponseEntity.ok(ResponseDTO.exito("Tipo de artículo encontrado", tipoArticulo)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseDTO.error("Tipo de artículo no encontrado", "404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener el tipo de artículo: " + e.getMessage()));
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ResponseDTO<List<TipoArticuloDTO>>> obtenerPorCategoria(@PathVariable TipoArticulo.CategoriaArticulo categoria) {
        try {
            List<TipoArticuloDTO> tiposArticulo = tipoArticuloService.obtenerPorCategoria(categoria);
            return ResponseEntity.ok(ResponseDTO.exito("Tipos de artículo por categoría obtenidos", tiposArticulo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Error al obtener tipos por categoría: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<TipoArticuloDTO>> crear(@Valid @RequestBody TipoArticuloDTO tipoArticuloDTO) {
        try {
            TipoArticuloDTO tipoArticuloCreado = tipoArticuloService.crear(tipoArticuloDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.exito("Tipo de artículo creado exitosamente", tipoArticuloCreado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al crear el tipo de artículo: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<TipoArticuloDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody TipoArticuloDTO tipoArticuloDTO) {
        try {
            TipoArticuloDTO tipoArticuloActualizado = tipoArticuloService.actualizar(id, tipoArticuloDTO);
            return ResponseEntity.ok(ResponseDTO.exito("Tipo de artículo actualizado exitosamente", tipoArticuloActualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al actualizar el tipo de artículo: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable Long id) {
        try {
            tipoArticuloService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.exito("Tipo de artículo eliminado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("Error al eliminar el tipo de artículo: " + e.getMessage()));
        }
    }
}