CREATE DATABASE gestorInsumo;

USE gestorInsumo;

Describe ROL;

CREATE TABLE ROL (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

insert into rol (nombre)values("ADMINISTRADOR");
insert into rol (nombre)values("CLIENTE");
insert into rol (nombre)values("TECNICO");

CREATE TABLE USUARIO (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE, -- Agregu茅 UNIQUE
    clave VARCHAR(255) NOT NULL, -- Aument茅 a 255 para BCrypt
    idRol INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT NULL,
    FOREIGN KEY (idRol) REFERENCES ROL(idRol),
    FOREIGN KEY (creado_por) REFERENCES USUARIO(idUsuario)
);

-- Agregar el rol ADMINISTRADOR si quieres mantener el código original
INSERT INTO ROL (nombre) VALUES ('ADMINISTRADOR');

-- Luego crear un usuario administrador
INSERT INTO USUARIO (nombre, apellido, email, clave, idRol, activo) 
VALUES (
    'Admin', 
    'Sistema', 
    'admin@test.com', 
    '$2a$10$N.zmdr9k7uOIYQaan.iK9.wqXF9UpuV2fzI3pf/e0D3D7VDAkqVgm', -- contraseña: 123456
    (SELECT idRol FROM ROL WHERE nombre = 'ADMINISTRADOR'), 
    true
);


DELETE FROM ROL;

select * FROM USUARIO;


-- Insertar rol admin si no existe
INSERT INTO ROL (nombre) VALUES ('ADMINISTRADOR');

-- Insertar usuario admin (la contraseña 'admin123' encriptada con BCrypt)
INSERT INTO USUARIO (nombre, apellido, email, clave, activo, fecha_creacion, idRol) 
VALUES (
    'Admin', 
    'Sistema', 
    'admin@test.com', 
    '$2a$10$DowjQerCvY1oJTlCLQ3g7eLdyq5v8XJoQe6ZK8/8tZKV.6vZBQQmC', -- password: admin123
    true, 
    CURDATE(), 
    1
);


SELECT u.*, r.nombre as rol_nombre 
FROM USUARIO u 
LEFT JOIN ROL r ON u.idRol = r.idRol;