-- Tabla de Sectores/Departamentos
CREATE TABLE sectores (
    idSector INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Empleados
CREATE TABLE empleados (
    idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE,
    email VARCHAR(100),
    telefono VARCHAR(20),
    idSector INT,
    activo TINYINT(1) DEFAULT 1,
    fecha_ingreso DATE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Estados (para todos los activos)
CREATE TABLE estados (
    idEstado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(100),
    activo TINYINT(1) DEFAULT 1
);

-- Insertar estados básicos
INSERT INTO estados (nombre, descripcion) VALUES 
('Activo', 'En funcionamiento normal'),
('Inactivo', 'Fuera de servicio temporalmente'),
('En Reparación', 'En proceso de reparación'),
('Dado de Baja', 'Fuera de servicio permanentemente');

-- =====================================================
-- EQUIPOS Y HARDWARE
-- =====================================================

-- Tabla de Dispositivos Móviles
CREATE TABLE dispositivos_moviles (
    idDispositivo INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    imei VARCHAR(20),
    numero_telefono VARCHAR(20),
    idEmpleado INT,
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Computadoras/Notebooks
CREATE TABLE computadoras (
    idComputadora INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    direccion_ip VARCHAR(15),
    usuario_sistema VARCHAR(100),
    contraseña_sistema VARCHAR(255),
    idEmpleado INT,
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Impresoras
CREATE TABLE impresoras (
    idImpresora INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    direccion_ip VARCHAR(15),
    ubicacion_fisica VARCHAR(200),
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_instalacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Scanners
CREATE TABLE scanners (
    idScanner INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    direccion_ip VARCHAR(15),
    idEmpleado INT,
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Teléfonos Internos
CREATE TABLE telefonos_internos (
    idTelefono INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    numero_interno VARCHAR(10) NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    ubicacion_fisica VARCHAR(200),
    idEmpleado INT,
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Monitores
CREATE TABLE monitores (
    idMonitor INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    tamaño VARCHAR(10),
    idEmpleado INT,
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Equipos de Red
CREATE TABLE equipos_red (
    idEquipoRed INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    tipo ENUM('Router', 'Switch', 'Access Point', 'Otro') NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    direccion_ip VARCHAR(15),
    usuario_admin VARCHAR(100),
    contraseña_admin VARCHAR(255),
    ubicacion_fisica VARCHAR(200),
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_instalacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Periféricos
CREATE TABLE perifericos (
    idPeriferico INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    tipo ENUM('Teclado', 'Mouse', 'Webcam', 'Auriculares', 'Otro') NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    idEmpleado INT,
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Equipos Audiovisuales
CREATE TABLE equipos_audiovisuales (
    idEquipoAV INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    tipo ENUM('Proyector', 'Pantalla', 'Parlantes', 'Micrófono', 'Otro') NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    ubicacion_fisica VARCHAR(200),
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_instalacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Dispositivos de Almacenamiento
CREATE TABLE dispositivos_almacenamiento (
    idAlmacenamiento INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    tipo ENUM('Disco Externo', 'USB', 'SSD', 'Otro') NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    capacidad VARCHAR(20),
    idEmpleado INT,
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Equipos UPS/Estabilizadores
CREATE TABLE equipos_energia (
    idEquipoEnergia INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    tipo ENUM('UPS', 'Estabilizador', 'Otro') NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    potencia VARCHAR(20),
    ubicacion_fisica VARCHAR(200),
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_instalacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Cámaras de Seguridad
CREATE TABLE camaras_seguridad (
    idCamara INT AUTO_INCREMENT PRIMARY KEY,
    codigo_activo VARCHAR(50) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    direccion_ip VARCHAR(15),
    usuario_admin VARCHAR(100),
    contraseña_admin VARCHAR(255),
    ubicacion_fisica VARCHAR(200),
    idSector INT,
    idEstado INT DEFAULT 1,
    fecha_instalacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- =====================================================
-- CREDENCIALES Y ACCESOS
-- =====================================================

-- Tabla de Correos Electrónicos
CREATE TABLE correos_electronicos (
    idCorreo INT AUTO_INCREMENT PRIMARY KEY,
    direccion_email VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    idEmpleado INT,
    idSector INT,
    tipo_cuenta ENUM('Personal', 'Compartida') DEFAULT 'Personal',
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion_cuenta DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Redes WiFi
CREATE TABLE redes_wifi (
    idWifi INT AUTO_INCREMENT PRIMARY KEY,
    nombre_red VARCHAR(100) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    zona_ubicacion VARCHAR(100),
    tipo_seguridad ENUM('WPA2', 'WPA3', 'WEP', 'Abierta') DEFAULT 'WPA2',
    idSector INT,
    activo TINYINT(1) DEFAULT 1,
    fecha_configuracion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Controladores de Cajas
CREATE TABLE controladores_cajas (
    idControlador INT AUTO_INCREMENT PRIMARY KEY,
    codigo_controlador VARCHAR(50) UNIQUE NOT NULL,
    direccion_ip VARCHAR(15) NOT NULL,
    usuario VARCHAR(100),
    contraseña VARCHAR(255),
    ubicacion_fisica VARCHAR(200),
    idSector INT,
    activo TINYINT(1) DEFAULT 1,
    fecha_configuracion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Plataformas de Servicios (Flow, Spotify, etc.)
CREATE TABLE plataformas_servicios (
    idPlataforma INT AUTO_INCREMENT PRIMARY KEY,
    nombre_plataforma VARCHAR(100) NOT NULL,
    tipo_servicio ENUM('Pago', 'Streaming', 'Red Social', 'Almacenamiento', 'Marketing', 'Bancario', 'Logística', 'Otro') NOT NULL,
    usuario VARCHAR(100) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    email_asociado VARCHAR(100),
    url_acceso VARCHAR(255),
    tipo_cuenta VARCHAR(50),
    idSector INT,
    activo TINYINT(1) DEFAULT 1,
    fecha_suscripcion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Sistemas Empresariales
CREATE TABLE sistemas_empresariales (
    idSistema INT AUTO_INCREMENT PRIMARY KEY,
    nombre_sistema VARCHAR(100) NOT NULL,
    tipo_sistema ENUM('CRM', 'ERP', 'Facturación', 'Contabilidad', 'Otro') NOT NULL,
    url_acceso VARCHAR(255),
    usuario VARCHAR(100) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    idEmpleado INT,
    idSector INT,
    rol_permisos VARCHAR(100),
    activo TINYINT(1) DEFAULT 1,
    fecha_asignacion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- Tabla de Servidores FTP
CREATE TABLE servidores_ftp (
    idServidor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_servidor VARCHAR(100) NOT NULL,
    direccion_ip VARCHAR(15),
    url_servidor VARCHAR(255),
    puerto INT DEFAULT 21,
    usuario VARCHAR(100) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    idSector INT,
    activo TINYINT(1) DEFAULT 1,
    fecha_configuracion DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- =====================================================
-- LICENCIAS DE SOFTWARE
-- =====================================================

-- Tabla de Licencias de Software
CREATE TABLE licencias_software (
    idLicencia INT AUTO_INCREMENT PRIMARY KEY,
    nombre_software VARCHAR(100) NOT NULL,
    tipo_software ENUM('Office', 'Antivirus', 'Adobe', 'AutoCAD', 'Especializado', 'Otro') NOT NULL,
    version VARCHAR(50),
    clave_producto VARCHAR(255),
    clave_licencia VARCHAR(255),
    email_asociado VARCHAR(100),
    tipo_licencia ENUM('Individual', 'Empresarial', 'Familiar', 'Educacional') DEFAULT 'Individual',
    idEmpleado INT,
    idSector INT,
    fecha_compra DATE,
    fecha_vencimiento DATE,
    activo TINYINT(1) DEFAULT 1,
    fecha_instalacion DATE,
    costo DECIMAL(10,2),
    proveedor VARCHAR(100),
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    actualizado_por INT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idSector) REFERENCES sectores(idSector),
    FOREIGN KEY (creado_por) REFERENCES usuario(idUsuario),
    FOREIGN KEY (actualizado_por) REFERENCES usuario(idUsuario)
);

-- =====================================================
-- TABLAS DE AUDITORÍA Y HISTORIAL
-- =====================================================

-- Tabla de Historial de Cambios (para auditoría)
CREATE TABLE historial_cambios (
    idHistorial INT AUTO_INCREMENT PRIMARY KEY,
    tabla_afectada VARCHAR(50) NOT NULL,
    id_registro INT NOT NULL,
    tipo_operacion ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    datos_anteriores JSON,
    datos_nuevos JSON,
    idUsuario INT,
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
);

-- =====================================================
-- ÍNDICES PARA MEJORAR PERFORMANCE
-- =====================================================

-- Índices para búsquedas frecuentes
CREATE INDEX idx_empleados_sector ON empleados(idSector);
CREATE INDEX idx_empleados_activo ON empleados(activo);
CREATE INDEX idx_dispositivos_empleado ON dispositivos_moviles(idEmpleado);
CREATE INDEX idx_dispositivos_sector ON dispositivos_moviles(idSector);
CREATE INDEX idx_computadoras_empleado ON computadoras(idEmpleado);
CREATE INDEX idx_computadoras_ip ON computadoras(direccion_ip);
CREATE INDEX idx_correos_empleado ON correos_electronicos(idEmpleado);
CREATE INDEX idx_licencias_vencimiento ON licencias_software(fecha_vencimiento);
CREATE INDEX idx_historial_tabla_registro ON historial_cambios(tabla_afectada, id_registro);

-- =====================================================
-- DATOS INICIALES BÁSICOS
-- =====================================================

-- Insertar algunos sectores básicos
INSERT INTO sectores (nombre, descripcion) VALUES 
('Administración', 'Sector administrativo'),
('Sistemas', 'Departamento de sistemas e IT'),
('Ventas', 'Departamento de ventas'),
('Contabilidad', 'Departamento de contabilidad'),
('Recursos Humanos', 'Departamento de RRHH');

-- =====================================================
-- VISTAS ÚTILES PARA REPORTES
-- =====================================================

-- Vista resumen de activos por empleado
CREATE VIEW vista_activos_empleado AS
SELECT 
    e.idEmpleado,
    CONCAT(e.nombre, ' ', e.apellido) as nombre_completo,
    s.nombre as sector,
    COUNT(dm.idDispositivo) as dispositivos_moviles,
    COUNT(c.idComputadora) as computadoras,
    COUNT(sc.idScanner) as scanners,
    COUNT(ti.idTelefono) as telefonos,
    COUNT(m.idMonitor) as monitores
FROM empleados e
LEFT JOIN sectores s ON e.idSector = s.idSector
LEFT JOIN dispositivos_moviles dm ON e.idEmpleado = dm.idEmpleado AND dm.idEstado = 1
LEFT JOIN computadoras c ON e.idEmpleado = c.idEmpleado AND c.idEstado = 1
LEFT JOIN scanners sc ON e.idEmpleado = sc.idEmpleado AND sc.idEstado = 1
LEFT JOIN telefonos_internos ti ON e.idEmpleado = ti.idEmpleado AND ti.idEstado = 1
LEFT JOIN monitores m ON e.idEmpleado = m.idEmpleado AND m.idEstado = 1
WHERE e.activo = 1
GROUP BY e.idEmpleado, e.nombre, e.apellido, s.nombre;

-- Vista de licencias próximas a vencer (30 días)
CREATE VIEW vista_licencias_vencimiento AS
SELECT 
    l.idLicencia,
    l.nombre_software,
    l.tipo_software,
    l.fecha_vencimiento,
    DATEDIFF(l.fecha_vencimiento, CURDATE()) as dias_restantes,
    CONCAT(emp.nombre, ' ', emp.apellido) as empleado_asignado,
    s.nombre as sector
FROM licencias_software l
LEFT JOIN empleados emp ON l.idEmpleado = emp.idEmpleado
LEFT JOIN sectores s ON l.idSector = s.idSector
WHERE l.activo = 1 
AND l.fecha_vencimiento IS NOT NULL 
AND l.fecha_vencimiento <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)
ORDER BY l.fecha_vencimiento ASC;