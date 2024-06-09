INSERT INTO tipos_usuarios (tipo_id, tipos_categoria)
VALUES 
    (1, 'Administrador'),
    (2, 'Responsable'),
    (3, 'Usuario');

INSERT INTO tipos_proyectar (tipo_id, tipos_categoria)
VALUES 
    (1, 'A Resolver'),
    (3, 'A Sentencia'),
    (2, 'A Regular');
   
INSERT INTO usuarios (user_id, tipo_id, user_apellido, user_nombre, user_mail)
VALUES
    (1, 1, 'Dominguez', 'Benjamin', 'benji86@gmail.com'),
    (2, 2, 'Stampalija', 'Juan Ignacio', 'juan@stampalija.com'),
    (3, 3, 'Fischetti', 'Haydee', 'haydee.fischetti@gmail.com'),
    (4, 3, 'Sena', 'Martin', 'senam@hotmail.com');

INSERT INTO criterios (criterio_id, criterioTema, criterioDescripcion)
VALUES 
    (1, 'honorarios', 'aplica ley vieja'),
    (2, 'ganancias', 'sumarisimo (art. 498)'),
    (3, 'ejecuciones', 'incompetencia, resolución y se remite'),
    (4, 'apelaciones', 'art. 246 y formamos incidente');

INSERT INTO expedientes (idExpediente, numeroExpediente, anioExpediente, actorExpediente, demandadaExpediente, objetoExpediente, user_id)
VALUES
    (1, 107, 2004, 'Dominguez Benjamin', 'YPF', 'daños y perjuicios', 1),
    (2, 32131, 2024, 'YPF', 'AFIP-DGA', 'proceso de conocimiento', 2),
    (3, 12123, 2023, 'Milagro Lopez', 'EN-M° Economia', 'empleo Publico', 3),
    (4, 5007, 2017, 'Juan Perez', 'EN-M° Economia', 'Daños y perjuicios', 1);

INSERT INTO consultas (consulta_id, idExpediente, consultaExpediente, consultaRespuestaExpediente, consultaCriterio)
VALUES
    (1, 1, 'Concedemos o no?', 'Si, art. 246', 4),
    (2, 3, 'Ley vieja o nueva', 'Ley vieja, fecha de inicio de la etapa', 1),
    (3, 4, 'Librar oficio o inhibir antes?', 'Inhibimos', NULL);