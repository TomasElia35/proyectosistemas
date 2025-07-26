package login.login.servicio;

import login.login.modelo.Usuario;
import login.login.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + mail));

        if (!usuario.getEstado()) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        // DEBUGGING: Verificar la contraseña
        System.out.println("=== DEBUG CustomUserDetailsService ===");
        System.out.println("🔍 Usuario encontrado: " + usuario.getMail());
        System.out.println("🔐 Contraseña almacenada: " + usuario.getContrasena());
        System.out.println("✅ Usuario activo: " + usuario.getEstado());
        System.out.println("👤 Rol: " + (usuario.getRol() != null ? usuario.getRol().getNombre() : "SIN ROL"));

        // TEST: Verificar si el hash es válido para "admin123"
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches("admin123", usuario.getContrasena());
        System.out.println("🧪 Test contraseña 'admin123': " + matches);

        // Generar un nuevo hash para comparar
        String newHash = encoder.encode("admin123");
        System.out.println("🆕 Nuevo hash generado: " + newHash);

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRol() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre().toUpperCase()));
            System.out.println("🔑 Autoridad asignada: ROLE_" + usuario.getRol().getNombre().toUpperCase());
        }

        return User.builder()
                .username(usuario.getMail())
                .password(usuario.getContrasena())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!usuario.getEstado())
                .credentialsExpired(false)
                .disabled(!usuario.getEstado())
                .build();
    }
}