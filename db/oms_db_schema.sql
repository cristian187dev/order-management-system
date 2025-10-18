-- Si existe la BD la borro, luego la creo desde cero y la uso.
DROP DATABASE IF EXISTS oms_db;
CREATE DATABASE oms_db;
USE oms_db;

-- Tabla: Tipo_usuario
CREATE TABLE Tipo_usuario (
    id_tipo_usuario INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL
);

-- Tabla: Usuarios
CREATE TABLE Usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    estado_usuario VARCHAR(20),
    fecha_inicio DATE,
    fecha_fin DATE,
    hash_clave VARCHAR(255),
    id_tipo_usuario INT,
    FOREIGN KEY (id_tipo_usuario) REFERENCES Tipo_usuario(id_tipo_usuario)
);

-- Tabla: Clientes
CREATE TABLE Clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    telefono VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(100),
    cuil VARCHAR(20),
    estado_usuario VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    fecha_inicio DATE,
    fecha_fin DATE DEFAULT NULL
);

-- Tabla: Productos
CREATE TABLE Productos (
    id_producto INT PRIMARY KEY AUTO_INCREMENT,
    nombre_producto VARCHAR(100),
    unidad_medida VARCHAR(20),
    stock_actual DECIMAL(10,2)
);

-- Tabla: Precios_Productos_Base
CREATE TABLE Precios_Productos_Base (
    id_precio_base INT PRIMARY KEY AUTO_INCREMENT,
    id_producto INT,
    precio_base DECIMAL(10,2),
    fecha_inicio_precio DATE,
    fecha_fin_precio DATE,
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

-- Tabla: Precios_Clientes
CREATE TABLE Precios_Clientes (
    id_precio_cliente INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    id_producto INT,
    precio_unitario DECIMAL(10,2),
    fecha_inicio_precio DATE,
    fecha_fin_precio DATE,
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente),
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

-- Tabla: Pedidos
CREATE TABLE Pedidos (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    estado_pago VARCHAR(20),
    fecha_pedido DATE,
    hora_pedido TIME,
    observaciones TEXT,
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);

-- Tabla: Detalles
CREATE TABLE Detalles (
    id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT,
    id_producto INT,
    cantidad DECIMAL(10,2),
    precio_unitario DECIMAL(10,2),
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

-- Tabla: Tipo_extra
CREATE TABLE Tipo_extra (
    id_tipo_extra INT PRIMARY KEY AUTO_INCREMENT,
    descripcion VARCHAR(100)
);

-- Tabla: Subpedidos
CREATE TABLE Subpedidos (
    id_subpedido INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT,
    nombre_subcliente VARCHAR(100),
    comentario TEXT,
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido)
);

-- Tabla: Detalles_Subpedidos
CREATE TABLE Detalles_Subpedidos (
    id_detalle_sub INT PRIMARY KEY AUTO_INCREMENT,
    id_subpedido INT,
    id_tipo_extra INT,
    id_producto INT, 
    cantidad DECIMAL(10,2),
    precio_unitario DECIMAL(10,2),
    FOREIGN KEY (id_subpedido) REFERENCES Subpedidos(id_subpedido),
    FOREIGN KEY (id_tipo_extra) REFERENCES Tipo_extra(id_tipo_extra),
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

-- Tabla: Facturas
CREATE TABLE Facturas (
    id_factura INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    monto_total_pedidos DECIMAL(10,2),
    fecha DATE,
    estado_factura VARCHAR(20),
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);

-- Tabla: Factura_Pedido
CREATE TABLE Factura_Pedido (
    id_factura INT,
    id_pedido INT,
    PRIMARY KEY (id_factura, id_pedido),
    FOREIGN KEY (id_factura) REFERENCES Facturas(id_factura),
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido)
);

-- Tabla: Pagos
CREATE TABLE Pagos (
    id_pago INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    id_factura INT,
    fecha_pago DATE,
    monto_pago DECIMAL(10,2),
    metodo_pago VARCHAR(50),
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente),
    FOREIGN KEY (id_factura) REFERENCES Facturas(id_factura)
);
