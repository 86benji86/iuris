CREATE TABLE expedientes (
    idExpediente INT PRIMARY KEY,
    numeroExpediente INT,
    anioExpediente INT, 
    actorExpediente VARCHAR(255),
    demandadaExpediente VARCHAR(255),
    objetoExpediente VARCHAR(255),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES usuarios(user_id)
);
