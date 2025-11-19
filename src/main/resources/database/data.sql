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
(1, 1, 1, '2025-01-10T14:32:00', '2025-01-10T14:40:00'),
(1, 3, 1, '2025-01-10T14:40:00', NULL);

-- Evento 2: AutoConfirmado → Confirmado
INSERT INTO cambio_estado (evento_id, estado_id, empleado_id, fecha_hora_inicio, fecha_hora_fin) VALUES
(2, 2, 2, '2025-02-05T09:10:00', '2025-02-05T09:25:00'),
(2, 3, 2, '2025-02-05T09:25:00', NULL);

-- Evento 3: AutoDetectado → Confirmado
INSERT INTO cambio_estado (evento_id, estado_id, empleado_id, fecha_hora_inicio, fecha_hora_fin) VALUES
(3, 1, 2, '2025-03-12T22:15:00', '2025-03-12T22:25:00'),
(3, 3, 1, '2025-03-12T22:25:00', NULL);
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