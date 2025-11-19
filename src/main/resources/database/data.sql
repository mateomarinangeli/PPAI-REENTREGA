------------------------------------------------------------
-- ESTADOS
------------------------------------------------------------
INSERT INTO estado (nombre) VALUES
('AutoDetectado'),
('AutoConfirmado'),
('Confirmado'),
('BloqueadoEnRevision'),
('Cerrado'),
('EventoSinRevision'),
('PendienteDeCierre'),
('PendienteDeRevision'),
('Rechazado');

------------------------------------------------------------
-- ALCANCE DEL SISMO
------------------------------------------------------------
INSERT INTO alcance_sismo (nombre, descripcion) VALUES
('Local', 'Evento registrado dentro de la región cercana a las estaciones.'),
('Regional', 'Movimiento perceptible en un rango medio.'),
('Tele Sismo', 'Sismo con epicentro a más de 1000 km.');

------------------------------------------------------------
-- CLASIFICACION SISMO
------------------------------------------------------------
INSERT INTO clasificacion_sismo (km_profundidad_desde, km_profundidad_hasta, nombre) VALUES
(0, 60, 'Superficial'),
(61, 300, 'Intermedio'),
(301, 650, 'Profundo');

------------------------------------------------------------
-- MAGNITUD RICHTER
------------------------------------------------------------
INSERT INTO magnitud_richter (numero, descripcion_magnitud) VALUES
(1.0, 'Prácticamente imperceptible incluso para instrumentos básicos.'),
(2.0, 'Muy leve, registrado por sismógrafos, no perceptible por personas.'),
(3.0, 'Leve, algunas personas en reposo pueden percibirlo.'),
(4.0, 'Perceptible, objetos pueden vibrar ligeramente, sin daños.'),
(5.0, 'Moderado, puede causar pequeños daños en estructuras débiles.'),
(6.0, 'Fuerte, daños moderados en edificios mal construidos.'),
(7.0, 'Muy fuerte, daños severos en zonas amplias.'),
(8.0, 'Destructivo, daños masivos, estructuras colapsan.'),
(9.0, 'Catastrófico, destrucción generalizada en área extensa.');

------------------------------------------------------------
-- ORIGEN GENERACION
------------------------------------------------------------
INSERT INTO origen_generacion (nombre, descripcion) VALUES
('Tectónico', 'Producido por fallas geológicas.'),
('Volcánico', 'Asociado a actividad volcánica.'),
('Inducido', 'Provocado por actividad humana.');

------------------------------------------------------------
-- EMPLEADOS
------------------------------------------------------------
INSERT INTO empleado (nombre, apellido, mail, telefono) VALUES
('Ana', 'Gómez', 'ana.gomez@instituto.gov', '3511111111'),
('Carlos', 'Rivas', 'carlos.rivas@instituto.gov', '3512222222'),
('Laura', 'Molina', 'laura.molina@instituto.gov', '3513333333');

------------------------------------------------------------
-- USUARIO
------------------------------------------------------------
INSERT INTO usuario (nombre_usuario, contrasena, id_empleado) VALUES
('agomez', '1234', 1),
('crivas', 'holis', 2),
('lmolina', 'laura123', 3);

------------------------------------------------------------
-- ESTACIONES SISMOLOGICAS
------------------------------------------------------------
INSERT INTO estacion_sismologica
    (codigo_estacion, nombre, latitud, longitud, fecha_solicitud_certificacion, nro_certificacion_adquisicion)
VALUES
('ST001', 'Estación Cerro Uritorco', -30.855, -64.509, '2023-01-15', 1001),
('ST002', 'Estación San Juan Norte', -31.553, -68.521, '2023-02-10', 1002);

------------------------------------------------------------
-- SISMÓGRAFOS
------------------------------------------------------------
INSERT INTO sismografo (identificador_sismografo, fecha_adquisicion, nro_serie, codigo_estacion) VALUES
(1, '2022-05-10', 55001, 'ST001'),
(2, '2022-06-12', 55002, 'ST002');

------------------------------------------------------------
-- SESIONES (SIMPLIFICADAS)
------------------------------------------------------------
INSERT INTO sesion (fecha_hora_inicio, fecha_hora_fin, id_usuario) VALUES
('2025-05-01 08:00', '2025-05-01 16:00', 1),
('2025-05-01 16:00', '2025-05-01 23:00', 2);

------------------------------------------------------------
-- EVENTO SISMICO (1 EJEMPLO REALISTA)
------------------------------------------------------------
------------------------------------------------------------
-- EVENTO SÍSMICO (3 eventos)
------------------------------------------------------------

INSERT INTO evento_sismico (
    fecha_hora_ocurrencia, fecha_hora_fin,
    latitud_epicentro, longitud_epicentro,
    latitud_hipocentro, longitud_hipocentro,
    valor_magnitud,
    analista_supervisor_id, estado_actual_id,
    alcance_id, origen_id, id_magnitud, clasificacion_id
)
VALUES
-- Evento 1
('2025-01-10T14:32:00', '2025-01-10T14:50:00',
 -31.42, -64.19,
 -31.50, -64.12,
 5.0,
 1, 1, 1, 1, 5, 1),

-- Evento 2
('2025-02-05T09:10:00', '2025-02-05T09:40:00',
 -27.78, -65.41,
 -27.85, -65.30,
 4.0,
 2, 2, 2, 1, 4, 2),

-- Evento 3
('2025-03-12T22:15:00', '2025-03-12T22:38:00',
 -32.95, -68.83,
 -33.02, -68.75,
 6.0,
 1, 1, 1, 1, 6, 3);


------------------------------------------------------------
-- CAMBIO DE ESTADO (HISTORIAL)
------------------------------------------------------------
------------------------------------------------------------
-- CAMBIO DE ESTADO (coherente con los 3 eventos)
------------------------------------------------------------

-- Evento 1: AutoDetectado → Confirmado
INSERT INTO cambio_estado (evento_id, estado_id, empleado_id, fecha_hora_inicio, fecha_hora_fin) VALUES
(1, 1, 1, '2025-01-10T14:32:00', NULL);

-- Evento 2: AutoConfirmado → Confirmado
INSERT INTO cambio_estado (evento_id, estado_id, empleado_id, fecha_hora_inicio, fecha_hora_fin) VALUES
(2, 2, 2, '2025-02-05T09:10:00', '2025-02-05T09:25:00'),
(2, 3, 2, '2025-02-05T09:25:00', NULL);

-- Evento 3: AutoDetectado → Confirmado
INSERT INTO cambio_estado (evento_id, estado_id, empleado_id, fecha_hora_inicio, fecha_hora_fin) VALUES
(3, 1, 2, '2025-03-12T22:15:00', NULL);
             -- Confirmado, actual

------------------------------------------------------------
-- SERIE TEMPORAL
------------------------------------------------------------
------------------------------------------------------------
-- SERIE TEMPORAL (3 por evento)
------------------------------------------------------------

-- Evento 1
INSERT INTO serie_temporal (
    evento_id, condicion_alarma, fecha_hora_inicio_registro,
    fecha_hora_registro, frecuencia_muestreo
) VALUES
(1, 0, '2025-01-10T14:32:00', '2025-01-10T14:33:00', 50),
(1, 0, '2025-01-10T14:33:00', '2025-01-10T14:34:00', 50),
(1, 1, '2025-01-10T14:34:00', '2025-01-10T14:35:00', 50);

-- Evento 2
INSERT INTO serie_temporal (
    evento_id, condicion_alarma, fecha_hora_inicio_registro,
    fecha_hora_registro, frecuencia_muestreo
) VALUES
(2, 0, '2025-02-05T09:10:00', '2025-02-05T09:12:00', 60),
(2, 0, '2025-02-05T09:12:00', '2025-02-05T09:14:00', 60),
(2, 1, '2025-02-05T09:14:00', '2025-02-05T09:16:00', 60);

-- Evento 3
INSERT INTO serie_temporal (
    evento_id, condicion_alarma, fecha_hora_inicio_registro,
    fecha_hora_registro, frecuencia_muestreo
) VALUES
(3, 0, '2025-03-12T22:15:00', '2025-03-12T22:16:00', 40),
(3, 0, '2025-03-12T22:16:00', '2025-03-12T22:17:00', 40),
(3, 1, '2025-03-12T22:17:00', '2025-03-12T22:18:00', 40);

-- ---------------------------------------------
-- Tipos de datos
-- ---------------------------------------------
INSERT INTO tipo_de_dato (denominacion, nombre_unidad_medida, valor_umbral) VALUES
('Velocidad de onda', 'm/s', 500.0),
('Frecuencia de onda', 'Hz', 50.0),
('Longitud de onda', 'm', 10.0);

-- ---------------------------------------------
-- Muestras sismicas (ejemplo)
-- ---------------------------------------------
-- Suponemos que existe una serie temporal con id_serie = 1
INSERT INTO muestra_sismica (fecha_hora_muestra, id_serie) VALUES
-- Serie 1
('2025-11-19 10:00:00', 1),
('2025-11-19 10:01:00', 1),
('2025-11-19 10:02:00', 1),

-- Serie 2
('2025-11-19 10:03:00', 2),
('2025-11-19 10:04:00', 2),
('2025-11-19 10:05:00', 2),

-- Serie 3
('2025-11-19 10:06:00', 3),
('2025-11-19 10:07:00', 3),
('2025-11-19 10:08:00', 3),

-- Serie 4
('2025-11-19 10:09:00', 4),
('2025-11-19 10:10:00', 4),
('2025-11-19 10:11:00', 4),

-- Serie 5
('2025-11-19 10:12:00', 5),
('2025-11-19 10:13:00', 5),
('2025-11-19 10:14:00', 5),

-- Serie 6
('2025-11-19 10:15:00', 6),
('2025-11-19 10:16:00', 6),
('2025-11-19 10:17:00', 6),

-- Serie 7
('2025-11-19 10:18:00', 7),
('2025-11-19 10:19:00', 7),
('2025-11-19 10:20:00', 7),

-- Serie 8
('2025-11-19 10:21:00', 8),
('2025-11-19 10:22:00', 8),
('2025-11-19 10:23:00', 8),

-- Serie 9
('2025-11-19 10:24:00', 9),
('2025-11-19 10:25:00', 9),
('2025-11-19 10:26:00', 9);


-- ---------------------------------------------
-- Detalle de las muestras
-- ---------------------------------------------
-- Cada muestra tiene 3 valores: velocidad, frecuencia, longitud
-- Supongamos que id_tipo_de_dato: 1=Velocidad, 2=Frecuencia, 3=Longitud
-- Serie 1 (muestras 1-3)
INSERT INTO detalle_muestra_sismica (id_muestra, valor, id_tipo_de_dato) VALUES
(1, 480.0, 1), (1, 48.0, 2), (1, 9.8, 3),
(2, 500.0, 1), (2, 50.0, 2), (2, 10.0, 3),
(3, 520.0, 1), (3, 52.0, 2), (3, 10.2, 3),

-- Serie 2 (muestras 4-6)
(4, 485.0, 1), (4, 49.0, 2), (4, 9.9, 3),
(5, 505.0, 1), (5, 51.0, 2), (5, 10.1, 3),
(6, 525.0, 1), (6, 53.0, 2), (6, 10.3, 3),

-- Serie 3 (muestras 7-9)
(7, 490.0, 1), (7, 50.0, 2), (7, 10.0, 3),
(8, 510.0, 1), (8, 52.0, 2), (8, 10.2, 3),
(9, 530.0, 1), (9, 54.0, 2), (9, 10.4, 3),

-- Serie 4 (muestras 10-12)
(10, 495.0, 1), (10, 51.0, 2), (10, 10.1, 3),
(11, 515.0, 1), (11, 53.0, 2), (11, 10.3, 3),
(12, 535.0, 1), (12, 55.0, 2), (12, 10.5, 3),

-- Serie 5 (muestras 13-15)
(13, 500.0, 1), (13, 52.0, 2), (13, 10.2, 3),
(14, 520.0, 1), (14, 54.0, 2), (14, 10.4, 3),
(15, 540.0, 1), (15, 56.0, 2), (15, 10.6, 3),

-- Serie 6 (muestras 16-18)
(16, 505.0, 1), (16, 53.0, 2), (16, 10.3, 3),
(17, 525.0, 1), (17, 55.0, 2), (17, 10.5, 3),
(18, 545.0, 1), (18, 57.0, 2), (18, 10.7, 3),

-- Serie 7 (muestras 19-21)
(19, 510.0, 1), (19, 54.0, 2), (19, 10.4, 3),
(20, 530.0, 1), (20, 56.0, 2), (20, 10.6, 3),
(21, 550.0, 1), (21, 58.0, 2), (21, 10.8, 3),

-- Serie 8 (muestras 22-24)
(22, 515.0, 1), (22, 55.0, 2), (22, 10.5, 3),
(23, 535.0, 1), (23, 57.0, 2), (23, 10.7, 3),
(24, 555.0, 1), (24, 59.0, 2), (24, 10.9, 3),

-- Serie 9 (muestras 25-27)
(25, 520.0, 1), (25, 56.0, 2), (25, 10.6, 3),
(26, 540.0, 1), (26, 58.0, 2), (26, 10.8, 3),
(27, 560.0, 1), (27, 60.0, 2), (27, 11.0, 3);

