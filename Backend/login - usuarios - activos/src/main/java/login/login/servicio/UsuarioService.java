package login.login.servicio;

import login.login.DTO.*;
import login.login.DTO.UsuarioResponsiveDTO;
import login.login.modelo.*;
import login.login.modelo.Usuario;
import login.login.repositorio.*;
import login.login.repositorio.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioResponsiveDTO crearUsuario(UsuarioRequestDTO usuarioRequest) {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByMail(usuarioRequest.getMail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Buscar el rol
        Rol rol = rolRepository.findById(usuarioRequest.getRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioRequest.getNombre());
        usuario.setApellido(usuarioRequest.getApellido());
        usuario.setMail(usuarioRequest.getMail());
        usuario.setContrasena(passwordEncoder.encode(usuarioRequest.getContrasena()));
        usuario.setEstado(usuarioRequest.getEstado());
        usuario.setRol(rol);

        usuario = usuarioRepository.save(usuario);

        return convertirAUsuarioResponsiveDTO(usuario);
    }

    public List<UsuarioResponsiveDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAUsuarioResponsiveDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponsiveDTO obtenerUsuarioPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return convertirAUsuarioResponsiveDTO(usuario);
    }

    public UsuarioResponsiveDTO actualizarUsuario(Integer id, UsuarioRequestDTO usuarioRequest) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el email ya existe y no es del mismo usuario
        if (usuarioRepository.existsByMail(usuarioRequest.getMail()) &&
                !usuario.getMail().equals(usuarioRequest.getMail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Buscar el rol
        Rol rol = rolRepository.findById(usuarioRequest.getRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setNombre(usuarioRequest.getNombre());
        usuario.setApellido(usuarioRequest.getApellido());
        usuario.setMail(usuarioRequest.getMail());

        // Solo actualizar contraseña si se proporciona una nueva
        if (usuarioRequest.getContrasena() != null && !usuarioRequest.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioRequest.getContrasena()));
        }

        usuario.setEstado(usuarioRequest.getEstado());
        usuario.setRol(rol);

        usuario = usuarioRepository.save(usuario);

        return convertirAUsuarioResponsiveDTO(usuario);
    }

    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public void cambiarEstadoUsuario(Integer id, Boolean estado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(estado);
        usuarioRepository.save(usuario);
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
