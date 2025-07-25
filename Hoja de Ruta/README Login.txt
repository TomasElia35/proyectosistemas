# Sistema de Login con Spring Boot

Este es un sistema de autenticación y autorización desarrollado con Spring Boot que implementa JWT para la seguridad.

## Configuración

### 1. Base de Datos
Ejecutar el script SQL de inicialización en tu base de datos MySQL:

```sql
-- Crear las tablas (según tu hoja de ruta)
CREATE TABLE ROL (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE USUARIO (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(255) NOT NULL,
    idRol INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT NULL,
    
    FOREIGN KEY (idRol) REFERENCES ROL(idRol),
    FOREIGN KEY (creado_por) REFERENCES USUARIO(idUsuario)
);
```

### 2. Dependencias
Asegúrate de agregar todas las dependencias adicionales al `pom.xml`.

### 3. Configuración
Actualiza el `application.properties` con las configuraciones adicionales proporcionadas.

### 4. Usuario Administrador
Ejecuta el script de inicialización para crear el usuario administrador por defecto:
- **Email**: admin@sistema.com
- **Contraseña**: admin123

## Endpoints Disponibles

### Autenticación
- `POST /api/auth/login` - Iniciar sesión

### Gestión de Usuarios (Solo Administradores)
- `GET /api/admin/usuarios` - Obtener todos los usuarios
- `POST /api/admin/usuarios` - Crear nuevo usuario
- `GET /api/admin/usuarios/{id}` - Obtener usuario por ID
- `PUT /api/admin/usuarios/{id}` - Actualizar usuario
- `DELETE /api/admin/usuarios/{id}` - Eliminar usuario
- `PATCH /api/admin/usuarios/{id}/estado?estado=true/false` - Cambiar estado del usuario

### Roles (Solo Administradores)
- `GET /api/admin/roles` - Obtener todos los roles

## Ejemplos de Uso

### Login
```json
POST /api/auth/login
{
    "mail": "admin@sistema.com",
    "contrasena": "admin123"
}
```

### Crear Usuario
```json
POST /api/admin/usuarios
Headers: Authorization: Bearer {token}
{
    "nombre": "Juan",
    "apellido": "Pérez",
    "mail": "juan.perez@example.com",
    "contrasena": "123456",
    "estado": true,
    "rol": 2
}
```

## Estructura del Proyecto

```
src/main/java/login/login/
├── DTO/
│   ├── UsuarioRequestDTO.java
│   ├── UsuarioResponsiveDTO.java
│   ├── LoginRequestDTO.java
│   └── LoginResponseDTO.java
├── configuracion/
│   ├── SecurityConfig.java
│   └── AppConfig.java
├── controlador/
│   ├── AuthController.java
│   ├── UsuarioController.java
│   └── RolController.java
├── excepcion/
│   └── GlobalExceptionHandler.java
├── modelo/
│   ├── Usuario.java
│   └── Rol.java
├── repositorio/
│   ├── UsuarioRepository.java
│   └── RolRepository.java
├── seguridad/
│   ├── JwtUtils.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtAuthenticationEntryPoint.java
└── servicio/
    ├── AuthService.java
    ├── UsuarioService.java
    └── CustomUserDetailsService.java
```

## Características Implementadas

- ✅ Autenticación con JWT
- ✅ Autorización basada en roles
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Validación de datos de entrada
- ✅ Manejo global de excepciones
- ✅ CORS configurado para frontend
- ✅ Endpoints RESTful
- ✅ Documentación de API

## Notas Importantes

1. **Seguridad**: Las contraseñas se encriptan con BCrypt antes de almacenarse.
2. **Tokens**: Los tokens JWT tienen una validez de 24 horas por defecto.
3. **Roles**: Solo los usuarios con rol "ADMINISTRADOR" pueden gestionar otros usuarios.
4. **Estado**: Los usuarios pueden ser activados/desactivados sin eliminarlos.

## Próximos Pasos

1. Integrar con el frontend Angular
2. Implementar refresh tokens
3. Agregar logging detallado
4. Implementar auditoría de cambios