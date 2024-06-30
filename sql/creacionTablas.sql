CREATE DATABASE IF NOT EXISTS iuris;
USE iuris;

CREATE TABLE IF NOT EXISTS expedientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL,
    anio INT NOT NULL,
    actor VARCHAR(255) NOT NULL,
    demandada VARCHAR(255) NOT NULL,
    objeto VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS consultas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_expediente INT NOT NULL,
    consulta TEXT NOT NULL,
    respuesta TEXT,
    FOREIGN KEY (id_expediente) REFERENCES expedientes(id)
);

CREATE TABLE IF NOT EXISTS aproyectar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_expediente INT NOT NULL,
    responsable VARCHAR(255),
    tipoProyectar VARCHAR(20) CHECK (tipoProyectar IN ('a resolver', 'a sentencia')),
    estadoProyectar VARCHAR(20),
    fechaProyeccion DATE,
    FOREIGN KEY (id_expediente) REFERENCES expedientes(id)
);

CREATE TABLE IF NOT EXISTS apelados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_expediente INT NOT NULL,
    notificado BOOLEAN NOT NULL,
    fecha_apelacion DATE NOT NULL,
    elevado BOOLEAN NOT NULL,
    fecha_elevado DATE,
    devueltos BOOLEAN NOT NULL,
    fecha_devuelto DATE,
    FOREIGN KEY (id_expediente) REFERENCES expedientes(id)
);

CREATE TABLE IF NOT EXISTS criterio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL
);
