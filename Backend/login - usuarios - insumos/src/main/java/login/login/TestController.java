package login.login;

import login.login.DTO.ApiResponseDTO;
import login.login.modelo.Rol;
import login.login.repositorio.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hash/{password}")
    public String generateHash(@PathVariable String password) {
        String hash = passwordEncoder.encode(password);

        System.out.println("=== GENERADOR DE HASH ===");
        System.out.println("Contraseña original: " + password);
        System.out.println("Hash generado: " + hash);

        // Verificar que el hash funciona
        boolean matches = passwordEncoder.matches(password, hash);
        System.out.println("Verificación: " + matches);

        return "{\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"hash\": \"" + hash + "\",\n" +
                "  \"verification\": " + matches + "\n" +
                "}";
    }

    @GetMapping("/verify/{password}/{hash}")
    public String verifyHash(@PathVariable String password, @PathVariable String hash) {
        boolean matches = passwordEncoder.matches(password, hash);

        System.out.println("=== VERIFICADOR DE HASH ===");
        System.out.println("Contraseña: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Coincide: " + matches);

        return "{\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"hash\": \"" + hash + "\",\n" +
                "  \"matches\": " + matches + "\n" +
                "}";
    }
    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/roles")
    public ResponseEntity<?> testRoles() {
        try {
            List<Rol> roles = rolRepository.findAll();
            System.out.println("🔍 Roles encontrados: " + roles.size());

            return ResponseEntity.ok(new ApiResponseDTO(true,
                    "Roles obtenidos (TEST)", roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, "Error: " + e.getMessage()));
        }
    }
}