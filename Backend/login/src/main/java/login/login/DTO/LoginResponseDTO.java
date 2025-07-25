package login.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private UsuarioResponsiveDTO usuario;
    private String mensaje;

    public LoginResponseDTO(String token, UsuarioResponsiveDTO usuario) {
        this.token = token;
        this.usuario = usuario;
        this.mensaje = "Login exitoso";
    }
}