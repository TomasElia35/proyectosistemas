package login.login.controlador;

import login.login.DTO.ApiResponseDTO;
import login.login.modelo.Rol;
import login.login.repositorio.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class RolController {

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosRoles() {
        try {
            List<Rol> roles = rolRepository.findAll();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Roles obtenidos exitosamente", roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }
}
