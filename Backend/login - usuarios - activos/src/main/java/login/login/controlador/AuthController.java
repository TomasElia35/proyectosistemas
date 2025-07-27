package login.login.controlador;

import login.login.DTO.ApiResponseDTO;
import login.login.DTO.LoginRequestDTO;
import login.login.DTO.LoginResponseDTO;
import login.login.servicio.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        // DEBUGGING: Ver qué datos llegan
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Mail recibido: " + loginRequest.getMail());
        System.out.println("Contraseña recibida: " + (loginRequest.getContrasena() != null ? "[PRESENTE]" : "[NULL]"));
        System.out.println("Longitud contraseña: " + (loginRequest.getContrasena() != null ? loginRequest.getContrasena().length() : 0));

        try {
            LoginResponseDTO response = authService.login(loginRequest);
            System.out.println("Login exitoso para: " + loginRequest.getMail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
            e.printStackTrace(); // Ver el stack trace completo
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }
}