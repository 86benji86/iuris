CREATE TABLE a_proyectar (
    a_proyectar_id INT PRIMARY KEY,
    idExpediente INT,
    FOREIGN KEY (idExpediente) REFERENCES expedientes(idExpediente),
    tipo_proyectar INT,
    FOREIGN KEY (tipo_proyectar) REFERENCES tipos_proyectar(tipo_id),
    a_proyectar_estado INT
);