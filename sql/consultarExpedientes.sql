SELECT
    EX.numeroExpediente AS 'Numero de expediente',
    EX.anioExpediente AS 'Año',
    EX.actorExpediente AS 'Actor',
    EX.demandadaExpediente AS 'Demandada',
    EX.objetoExpediente AS 'Objeto'
FROM
    expedientes EX
WHERE
    EX.anioExpediente > 2020


SELECT
    EX.numeroExpediente AS 'Numero de expediente',
    EX.anioExpediente AS 'Año',
    EX.actorExpediente AS 'Actor',
    EX.demandadaExpediente AS 'Demandada',
    EX.objetoExpediente AS 'Objeto',
    CO.consultaExpediente AS 'Consulta',
    CO.consultaRespuestaExpediente AS 'Respuesta'
FROM
    expedientes EX,
    consultas CO
WHERE
    EX.idExpediente = CO.idExpediente
    AND EX.anioExpediente < 2020;
