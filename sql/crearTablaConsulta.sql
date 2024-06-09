CREATE TABLE consultas (
    consulta_id INT PRIMARY KEY,
    idExpediente INT,
    FOREIGN KEY (idExpediente) REFERENCES expedientes(idExpediente),
    consultaExpediente VARCHAR(255),
    consultaRespuestaExpediente VARCHAR(255),
    consultaCriterio INT,
    FOREIGN KEY (consultaCriterio) REFERENCES criterios(criterio_id)
);