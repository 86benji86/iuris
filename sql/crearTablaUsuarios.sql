CREATE TABLE usuarios (
    user_id INT PRIMARY KEY,
    tipo_id INT,
    FOREIGN KEY (tipo_id) REFERENCES tipos_usuarios(tipo_id),
    user_apellido VARCHAR(255),
    user_nombre VARCHAR(255),
    user_mail VARCHAR(255)
);
