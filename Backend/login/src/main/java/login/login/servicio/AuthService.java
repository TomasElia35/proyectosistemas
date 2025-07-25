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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getMail(),
                            loginRequest.getContrasena()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            Usuario usuario = usuarioRepository.findByMail(loginRequest.getMail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            UsuarioResponsiveDTO usuarioDTO = convertirAUsuarioResponsiveDTO(usuario);

            return new LoginResponseDTO(jwt, usuarioDTO);

        } catch (Exception e) {
            throw new RuntimeException("Credenciales inválidas");
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

