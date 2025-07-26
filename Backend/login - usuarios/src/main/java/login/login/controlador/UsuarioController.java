package login.login.controlador;

import login.login.DTO.ApiResponseDTO;
import login.login.DTO.UsuarioRequestDTO;
import login.login.DTO.UsuarioResponsiveDTO;
import login.login.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        try {
            UsuarioResponsiveDTO usuario = usuarioService.crearUsuario(usuarioRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Usuario creado exitosamente", usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosUsuarios() {
        try {
            List<UsuarioResponsiveDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Usuarios obtenidos exitosamente", usuarios));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Integer id) {
        try {
            UsuarioResponsiveDTO usuario = usuarioService.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Usuario obtenido exitosamente", usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id,
                                               @Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        try {
            UsuarioResponsiveDTO usuario = usuarioService.actualizarUsuario(id, usuarioRequest);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Usuario actualizado exitosamente", usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Usuario eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoUsuario(@PathVariable Integer id,
                                                  @RequestParam Boolean estado) {
        try {
            usuarioService.cambiarEstadoUsuario(id, estado);
            String mensaje = estado ? "Usuario activado" : "Usuario desactivado";
            return ResponseEntity.ok(new ApiResponseDTO(true, mensaje));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }
}