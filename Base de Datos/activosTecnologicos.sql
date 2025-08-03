USE GestorInsumo;

-- ====================================
-- 1. TABLAS DE SOPORTE (MAESTROS)
-- ====================================

-- Tabla: TipoArticulo
CREATE TABLE TipoArticulo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    categoria ENUM('HARDWARE', 'SOFTWARE', 'PERIFERICO', 'ACCESORIO') NOT NULL,
    requiereNumeroSerie BOOLEAN DEFAULT TRUE,
    vidaUtilDefectoAnios INT DEFAULT 3,
    requiereMantenimiento BOOLEAN DEFAULT FALSE,
    esActivo BOOLEAN DEFAULT TRUE,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_categoria (categoria),
    INDEX idx_activo (esActivo)
);

-- Tabla: Estado
CREATE TABLE Estado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    permiteAsignacion BOOLEAN DEFAULT TRUE,
    requiereAprobacion BOOLEAN DEFAULT FALSE,
    esEstadoFinal BOOLEAN DEFAULT FALSE,
    esActivo BOOLEAN DEFAULT TRUE,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_permite_asignacion (permiteAsignacion),
    INDEX idx_activo (esActivo)
);

-- Tabla: Area
CREATE TABLE Area (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    centroCosto VARCHAR(50),
    idResponsable INT NULL,
    ubicacionFisica VARCHAR(200),
    esActiva BOOLEAN DEFAULT TRUE,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_responsable (idResponsable),
    INDEX idx_activa (esActiva),
    INDEX idx_centro_costo (centroCosto)
);

-- Tabla: Empleado
CREATE TABLE Empleado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    legajo VARCHAR(20) NOT NULL UNIQUE,
    dni VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fechaNacimiento DATE,
    emailCorporativo VARCHAR(150) UNIQUE,
    celularCorporativo VARCHAR(20),
    idArea INT NOT NULL,
    fechaIngreso DATE NOT NULL,
    fechaBaja DATE NULL,
    tipoContrato ENUM('PLANTA', 'CONTRATO', 'PASANTE', 'TERCERO') NOT NULL,
    esActivo BOOLEAN DEFAULT TRUE,
    esResponsableArea BOOLEAN DEFAULT FALSE,
    observaciones TEXT,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (idArea) REFERENCES Area(id),
    INDEX idx_legajo (legajo),
    INDEX idx_dni (dni),
    INDEX idx_area (idArea),
    INDEX idx_activo (esActivo),
    INDEX idx_tipo_contrato (tipoContrato)
);

-- Tabla: Proveedor
CREATE TABLE Proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    cuit VARCHAR(15) NOT NULL UNIQUE,
    emailContacto VARCHAR(150),
    telefonoContacto VARCHAR(20),
    nombreContactoPrincipal VARCHAR(100),
    direccion TEXT,
    formaPago ENUM('CONTADO', 'CUENTA_CORRIENTE', 'CHEQUE', 'TRANSFERENCIA') DEFAULT 'CUENTA_CORRIENTE',
    monedaOperacion ENUM('ARS', 'USD', 'EUR') DEFAULT 'ARS',
    diasPagoPromedio INT DEFAULT 30,
    esActivo BOOLEAN DEFAULT TRUE,
    fechaAlta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    
    INDEX idx_cuit (cuit),
    INDEX idx_activo (esActivo),
    INDEX idx_forma_pago (formaPago)
);

-- ====================================
-- 2. TABLA PRINCIPAL DE ACTIVOS
-- ====================================

-- Tabla: ActivosTecnologicos
CREATE TABLE ActivosTecnologicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigoInventario VARCHAR(50) NOT NULL UNIQUE,
    numeroSerie VARCHAR(100) UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    idTipoArticulo INT NOT NULL,
    idProveedor INT NOT NULL,
    costo DECIMAL(15,2) CHECK (costo >= 0),
    fechaAdquisicion DATE NOT NULL,
    garantiaMeses INT DEFAULT 0 CHECK (garantiaMeses >= 0),
    fechaVencimientoGarantia DATE,
    vidaUtilAnios INT DEFAULT 3,
    valorLibros DECIMAL(15,2),
    ubicacionFisica VARCHAR(200),
    especificacionesTecnicas JSON,
    idEstado INT NOT NULL,
    idArea INT NOT NULL,
    observaciones TEXT,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fechaUltimaModificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    usuarioCreacion VARCHAR(100) NOT NULL,
    usuarioModificacion VARCHAR(100),
    esActivo BOOLEAN DEFAULT TRUE,
    
    FOREIGN KEY (idTipoArticulo) REFERENCES TipoArticulo(id),
    FOREIGN KEY (idProveedor) REFERENCES Proveedor(id),
    FOREIGN KEY (idEstado) REFERENCES Estado(id),
    FOREIGN KEY (idArea) REFERENCES Area(id),
    
    INDEX idx_codigo_inventario (codigoInventario),
    INDEX idx_numero_serie (numeroSerie),
    INDEX idx_tipo_articulo (idTipoArticulo),
    INDEX idx_proveedor (idProveedor),
    INDEX idx_estado (idEstado),
    INDEX idx_area (idArea),
    INDEX idx_marca_modelo (marca, modelo),
    INDEX idx_fecha_adquisicion (fechaAdquisicion),
    INDEX idx_activo (esActivo)
);

-- ====================================
-- 3. HISTORIAL DE ASIGNACIONES
-- ====================================

-- Tabla: AsignacionesActivos
CREATE TABLE AsignacionesActivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idActivo INT NOT NULL,
    idEmpleado INT NOT NULL,
    idArea INT NOT NULL,
    fechaAsignacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaDesasignacion TIMESTAMP NULL,
    motivoDesasignacion VARCHAR(500),
    observaciones TEXT,
    usuarioAsignacion VARCHAR(100) NOT NULL,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (idActivo) REFERENCES ActivosTecnologicos(id),
    FOREIGN KEY (idEmpleado) REFERENCES Empleado(id),
    FOREIGN KEY (idArea) REFERENCES Area(id),
    
    INDEX idx_activo (idActivo),
    INDEX idx_empleado (idEmpleado),
    INDEX idx_area (idArea),
    INDEX idx_fecha_asignacion (fechaAsignacion),
    INDEX idx_asignacion_activa (fechaDesasignacion) -- NULL para asignaciones activas
);

-- ====================================
-- 4. CONTROL DE MOVIMIENTOS (AUDITORÍA)
-- ====================================

-- Tabla: MovimientosActivos
CREATE TABLE MovimientosActivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idActivo INT NOT NULL,
    tipoMovimiento ENUM('ASIGNACION', 'DEVOLUCION', 'TRANSFERENCIA', 'MANTENIMIENTO', 'BAJA') NOT NULL,
    fechaMovimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idEmpleadoOrigen INT NULL,
    idEmpleadoDestino INT NULL,
    idAreaOrigen INT NULL,
    idAreaDestino INT NULL,
    idEstadoAnterior INT NOT NULL,
    idEstadoNuevo INT NOT NULL,
    motivoMovimiento VARCHAR(500) NOT NULL,
    observaciones TEXT,
    usuarioMovimiento VARCHAR(100) NOT NULL,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (idActivo) REFERENCES ActivosTecnologicos(id),
    FOREIGN KEY (idEmpleadoOrigen) REFERENCES Empleado(id),
    FOREIGN KEY (idEmpleadoDestino) REFERENCES Empleado(id),
    FOREIGN KEY (idAreaOrigen) REFERENCES Area(id),
    FOREIGN KEY (idAreaDestino) REFERENCES Area(id),
    FOREIGN KEY (idEstadoAnterior) REFERENCES Estado(id),
    FOREIGN KEY (idEstadoNuevo) REFERENCES Estado(id),
    
    INDEX idx_activo (idActivo),
    INDEX idx_tipo_movimiento (tipoMovimiento),
    INDEX idx_fecha_movimiento (fechaMovimiento),
    INDEX idx_empleado_origen (idEmpleadoOrigen),
    INDEX idx_empleado_destino (idEmpleadoDestino),
    INDEX idx_area_origen (idAreaOrigen),
    INDEX idx_area_destino (idAreaDestino)
);

-- ====================================
-- 5. GESTIÓN DE MANTENIMIENTOS
-- ====================================

-- Tabla: MantenimientosActivos
CREATE TABLE MantenimientosActivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idActivo INT NOT NULL,
    tipoMantenimiento ENUM('PREVENTIVO', 'CORRECTIVO', 'GARANTIA', 'UPGRADE') NOT NULL,
    fechaProgramada DATE NOT NULL,
    fechaRealizada DATE NULL,
    idProveedor INT NULL,
    tecnicoAsignado VARCHAR(150),
    costo DECIMAL(10,2) DEFAULT 0 CHECK (costo >= 0),
    descripcionProblema TEXT,
    descripcionSolucion TEXT,
    repuestosUtilizados TEXT,
    horasFueraServicio INT DEFAULT 0,
    idEstadoResultante INT NOT NULL,
    observaciones TEXT,
    usuarioCreacion VARCHAR(100) NOT NULL,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (idActivo) REFERENCES ActivosTecnologicos(id),
    FOREIGN KEY (idProveedor) REFERENCES Proveedor(id),
    FOREIGN KEY (idEstadoResultante) REFERENCES Estado(id),
    
    INDEX idx_activo (idActivo),
    INDEX idx_tipo_mantenimiento (tipoMantenimiento),
    INDEX idx_fecha_programada (fechaProgramada),
    INDEX idx_fecha_realizada (fechaRealizada),
    INDEX idx_proveedor (idProveedor)
);

-- ====================================
-- 6. RELACIÓN CON USUARIOS EXISTENTES
-- ====================================

-- Agregar FK a Area para responsable (referencia a usuario existente)
ALTER TABLE Area ADD CONSTRAINT fk_area_responsable 
FOREIGN KEY (idResponsable) REFERENCES usuario(idUsuario);

-- ====================================
-- 7. DATOS INICIALES BÁSICOS
-- ====================================

-- Estados básicos del sistema
INSERT INTO Estado (nombre, descripcion, permiteAsignacion, requiereAprobacion, esEstadoFinal) VALUES
('DISPONIBLE', 'Activo disponible para asignación', TRUE, FALSE, FALSE),
('ASIGNADO', 'Activo asignado a empleado', FALSE, FALSE, FALSE),
('EN_MANTENIMIENTO', 'Activo en proceso de mantenimiento', FALSE, FALSE, FALSE),
('FUERA_SERVICIO', 'Activo fuera de servicio temporalmente', FALSE, TRUE, FALSE),
('BAJA_DEFINITIVA', 'Activo dado de baja permanentemente', FALSE, TRUE, TRUE),
('EN_REPARACION', 'Activo en reparación externa', FALSE, FALSE, FALSE),
('OBSOLETO', 'Activo obsoleto pero funcional', TRUE, TRUE, FALSE);

-- Tipos de artículos básicos
INSERT INTO TipoArticulo (nombre, descripcion, categoria, requiereNumeroSerie, vidaUtilDefectoAnios, requiereMantenimiento) VALUES
('Computadora de escritorio', 'PC de escritorio para oficina', 'HARDWARE', TRUE, 5, TRUE),
('Notebook', 'Computadora portátil', 'HARDWARE', TRUE, 4, TRUE),
('Monitor', 'Monitor LCD/LED para computadora', 'PERIFERICO', TRUE, 6, FALSE),
('Impresora', 'Impresora láser o de inyección', 'PERIFERICO', TRUE, 3, TRUE),
('Teclado', 'Teclado USB/PS2', 'ACCESORIO', FALSE, 2, FALSE),
('Mouse', 'Mouse óptico/láser', 'ACCESORIO', FALSE, 2, FALSE),
('Software', 'Licencias de software', 'SOFTWARE', FALSE, 1, FALSE),
('Router', 'Equipo de red router', 'HARDWARE', TRUE, 5, TRUE),
('Switch', 'Switch de red', 'HARDWARE', TRUE, 5, TRUE),
('Proyector', 'Proyector multimedia', 'PERIFERICO', TRUE, 4, TRUE);

-- Áreas básicas (ejemplos)
INSERT INTO Area (nombre, descripcion, centroCosto, ubicacionFisica) VALUES
('Sistemas', 'Área de Tecnología y Sistemas', 'CC001', 'Piso 2 - Oficina 201'),
('Administración', 'Área Administrativa', 'CC002', 'Piso 1 - Oficina 101'),
('Recursos Humanos', 'Área de RRHH', 'CC003', 'Piso 1 - Oficina 105'),
('Deposito', 'Depósito de equipos', 'CC999', 'Planta Baja - Depósito'),
('Gerencia', 'Gerencia General', 'CC000', 'Piso 3 - Oficina 301');
