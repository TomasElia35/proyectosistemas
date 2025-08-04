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
    activo BOOLEAN DEFAULT TRUE,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_categoria (categoria),
    INDEX idx_activo (activo)
);

-- Tabla: Estado
CREATE TABLE Estado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    permiteAsignacion BOOLEAN DEFAULT TRUE,
    requiereAprobacion BOOLEAN DEFAULT FALSE,
    esEstadoFinal BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_permite_asignacion (permiteAsignacion),
    INDEX idx_activo (activo)
);

-- Tabla: Area
CREATE TABLE Area (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    idResponsable INT NULL,
    ubicacionFisica ENUM('HERGO', 'ALTA DISTRIBUCION', 'SALON', 'DISTRITINO','COUSTLAIN','MENOR COSTE ALBERTI','MENOR COSTE ALEM','MENOR COSTE CORDOBA','MENOR COSTE CARILO','LA CHATA') NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_responsable (idResponsable),
    INDEX idx_activa (activa)
);

-- Tabla: Empleado
CREATE TABLE Empleado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fechaNacimiento DATE,
    emailCorporativo VARCHAR(150) UNIQUE,
    celularCorporativo VARCHAR(20),
    idArea INT NOT NULL,
    fechaIngreso DATE NOT NULL,
    fechaBaja DATE NULL,
    tipoContrato ENUM('EFECTIVO', 'EVENTUAL','PART TIME', 'PASANTE', 'EXTERNO') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    esResponsableArea BOOLEAN DEFAULT FALSE,
    observaciones TEXT,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (idArea) REFERENCES Area(id),
    INDEX idx_dni (dni),
    INDEX idx_area (idArea),
    INDEX idx_activo (activo),
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
    monedaOperacion ENUM('ARS', 'USD') DEFAULT 'ARS',
    diasPagoPromedio INT DEFAULT 30,
    activo BOOLEAN DEFAULT TRUE,
    fechaAlta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    
    INDEX idx_cuit (cuit),
    INDEX idx_activo (activo),
    INDEX idx_forma_pago (formaPago)
);

-- Tabla: ModeloArticulo
CREATE TABLE ModeloArticulo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    idTipoArticulo INT NOT NULL,
    
    UNIQUE (marca, modelo),
    FOREIGN KEY (idTipoArticulo) REFERENCES TipoArticulo(id)
);

-- ====================================
-- 2. TABLA PRINCIPAL DE ACTIVOS
-- ====================================

-- Tabla: ActivosTecnologicos
CREATE TABLE ActivosTecnologicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigoInventario VARCHAR(50) NOT NULL UNIQUE,
    codigoTecnico VARCHAR(100) UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
	idModeloArticulo INT NOT NULL,
    idProveedor INT NOT NULL,
    costo DECIMAL(15,2) CHECK (costo >= 0),
    fechaAdquisicion DATE NOT NULL,
    garantiaMeses INT DEFAULT 0 CHECK (garantiaMeses >= 0),
    fechaVencimientoGarantia DATE,
    idEstado INT NOT NULL,
    idArea INT NOT NULL,
    observaciones TEXT,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fechaUltimaModificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    usuarioCreacion VARCHAR(100) NOT NULL,
    usuarioModificacion VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    
    
    FOREIGN KEY (idModeloArticulo) REFERENCES ModeloArticulo(id),
    FOREIGN KEY (idProveedor) REFERENCES Proveedor(id),
    FOREIGN KEY (idEstado) REFERENCES Estado(id),
    FOREIGN KEY (idArea) REFERENCES Area(id),
    
    INDEX idx_codigo_inventario (codigoInventario),
    INDEX idx_codigo_tecnico (codigoTecnico),
    INDEX idx_modelo_articulo (idModeloArticulo),
    INDEX idx_proveedor (idProveedor),
    INDEX idx_estado (idEstado),
    INDEX idx_area (idArea),
    INDEX idx_fecha_adquisicion (fechaAdquisicion),
    INDEX idx_activo (activo)
);

-- MODIFICACION ASIGNACION A EMPLEADO Y NO A AREA DE ACTIVO TECNOLOGICO
-- 1. Eliminar la constraint de clave foránea existente
ALTER TABLE ActivosTecnologicos
  DROP FOREIGN KEY activostecnologicos_ibfk_4;

-- 2. Eliminar la columna
ALTER TABLE ActivosTecnologicos
  DROP COLUMN idArea;

-- 3. Agregar columna y clave foránea a Empleado
ALTER TABLE ActivosTecnologicos
  ADD COLUMN idEmpleado INT NOT NULL,
  ADD CONSTRAINT fk_activo_empleado FOREIGN KEY (idEmpleado) REFERENCES Empleado(id);

