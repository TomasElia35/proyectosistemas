package login.login.servicio;

import login.login.DTO.*;
import login.login.modelo.Usuario;
import login.login.repositorio.UsuarioRepository;
import login.login.seguridad.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ModelMapper modelMapper;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // DEBUGGING: Verificar si el usuario existe antes de autenticar
            System.out.println("=== DEBUG AUTH SERVICE ===");
            System.out.println("Buscando usuario con email: " + loginRequest.getMail());

            Usuario usuarioExiste = usuarioRepository.findByMail(loginRequest.getMail()).orElse(null);
            if (usuarioExiste == null) {
                System.out.println("ERROR: Usuario no encontrado en BD");
                throw new RuntimeException("Usuario no encontrado");
            } else {
                System.out.println("Usuario encontrado: " + usuarioExiste.getNombre() + " " + usuarioExiste.getApellido());
                System.out.println("Estado del usuario: " + usuarioExiste.getEstado());
                System.out.println("Rol del usuario: " + (usuarioExiste.getRol() != null ? usuarioExiste.getRol().getNombre() : "SIN ROL"));
            }

            // Intentar autenticación
            System.out.println("Intentando autenticación...");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getMail(),
                            loginRequest.getContrasena()
                    )
            );
            System.out.println("Autenticación exitosa!");

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            Usuario usuario = usuarioRepository.findByMail(loginRequest.getMail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            UsuarioResponsiveDTO usuarioDTO = convertirAUsuarioResponsiveDTO(usuario);

            return new LoginResponseDTO(jwt, usuarioDTO);

        } catch (Exception e) {
            System.out.println("Error detallado: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Credenciales inválidas: " + e.getMessage());
        }
    }

    private UsuarioResponsiveDTO convertirAUsuarioResponsiveDTO(Usuario usuario) {
        UsuarioResponsiveDTO dto = modelMapper.map(usuario, UsuarioResponsiveDTO.class);

        if (usuario.getRol() != null) {
            UsuarioResponsiveDTO.RolDTO rolDTO = new UsuarioResponsiveDTO.RolDTO();
            rolDTO.setId(usuario.getRol().getId());
            rolDTO.setNombre(usuario.getRol().getNombre());
            dto.setRol(rolDTO);
        }

        return dto;
    }
}

