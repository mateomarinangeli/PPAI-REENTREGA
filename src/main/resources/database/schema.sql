PRAGMA foreign_keys = ON;

------------------------------------------------------------
-- TABLAS BÁSICAS
------------------------------------------------------------

-- AlcanceSismo (necesita ID)
CREATE TABLE alcance_sismo (
    id_alcance INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT
);

-- ClasificacionSismo (necesita ID)
CREATE TABLE clasificacion_sismo (
    id_clasificacion INTEGER PRIMARY KEY AUTOINCREMENT,
    km_profundidad_desde INTEGER NOT NULL,
    km_profundidad_hasta INTEGER NOT NULL,
    nombre TEXT NOT NULL
);

-- Empleado (necesita ID)
CREATE TABLE empleado (
    id_empleado INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    mail TEXT NOT NULL,
    telefono TEXT
);

-- MagnitudRichter (es compartida, necesita ID)
CREATE TABLE magnitud_richter (
    id_magnitud INTEGER PRIMARY KEY AUTOINCREMENT,
    numero REAL NOT NULL,
    descripcion_magnitud TEXT
);

-- Origen de generación (necesita ID)
CREATE TABLE origen_generacion (
    id_origen INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT
);

------------------------------------------------------------
-- ESTADO (State Pattern)
------------------------------------------------------------

-- Estados concretos (AutoDetectado, Confirmado, etc.)
CREATE TABLE estado (
    id_estado INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

------------------------------------------------------------
-- ESTACION, SISMÓGRAFO Y SESIÓN
------------------------------------------------------------

-- Estación sismológica (PK = codigoEstacion)
CREATE TABLE estacion_sismologica (
    codigo_estacion TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    latitud REAL NOT NULL,
    longitud REAL NOT NULL,
    fecha_solicitud_certificacion TEXT,
    nro_certificacion_adquisicion INTEGER
);

-- Sismografo (PK = identificadorSismografo)
CREATE TABLE sismografo (
    identificador_sismografo INTEGER PRIMARY KEY,
    fecha_adquisicion TEXT,
    nro_serie INTEGER,
    codigo_estacion TEXT NOT NULL,
    FOREIGN KEY (codigo_estacion) REFERENCES estacion_sismologica(codigo_estacion)
);

------------------------------------------------------------
-- USUARIO Y SESIÓN
------------------------------------------------------------

-- Usuario (necesita ID)
CREATE TABLE usuario (
    id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_usuario TEXT NOT NULL UNIQUE,
    contrasena TEXT NOT NULL,
    id_empleado INTEGER NOT NULL,
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

-- Sesión
CREATE TABLE sesion (
    id_sesion INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_hora_inicio TEXT NOT NULL,
    fecha_hora_fin TEXT,
    id_usuario INTEGER NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

------------------------------------------------------------
-- EVENTO SÍSMICO
------------------------------------------------------------

CREATE TABLE evento_sismico (
    id_evento INTEGER PRIMARY KEY AUTOINCREMENT,

    fecha_hora_ocurrencia TEXT NOT NULL,
    fecha_hora_fin TEXT,

    latitud_epicentro REAL NOT NULL,
    longitud_epicentro REAL NOT NULL,
    latitud_hipocentro REAL NOT NULL,
    longitud_hipocentro REAL NOT NULL,

    valor_magnitud REAL,

    -- FKs a entidades relacionadas
    analista_supervisor_id INTEGER NOT NULL,
    estado_actual_id INTEGER NOT NULL,
    alcance_id INTEGER NOT NULL,
    origen_id INTEGER NOT NULL,
    id_magnitud INTEGER NOT NULL,
    clasificacion_id INTEGER NOT NULL,

    FOREIGN KEY (analista_supervisor_id) REFERENCES empleado(id_empleado),
    FOREIGN KEY (estado_actual_id) REFERENCES estado(id_estado),
    FOREIGN KEY (alcance_id) REFERENCES alcance_sismo(id_alcance),
    FOREIGN KEY (origen_id) REFERENCES origen_generacion(id_origen),
    FOREIGN KEY (id_magnitud) REFERENCES magnitud_richter(id_magnitud),
    FOREIGN KEY (clasificacion_id) REFERENCES clasificacion_sismo(id_clasificacion)
);

------------------------------------------------------------
-- CAMBIO DE ESTADO (HISTORIAL)
------------------------------------------------------------

CREATE TABLE cambio_estado (
    id_cambio_estado INTEGER PRIMARY KEY AUTOINCREMENT,
    evento_id INTEGER NOT NULL,
    estado_id INTEGER NOT NULL,
    empleado_id INTEGER,
    fecha_hora_inicio TEXT NOT NULL,
    fecha_hora_fin TEXT,
    FOREIGN KEY (evento_id) REFERENCES evento_sismico(id_evento),
    FOREIGN KEY (estado_id) REFERENCES estado(id_estado),
    FOREIGN KEY (empleado_id) REFERENCES empleado(id_empleado)
);

------------------------------------------------------------
-- SERIE TEMPORAL (1 a muchos desde Evento)
------------------------------------------------------------

CREATE TABLE serie_temporal (
    id_serie INTEGER PRIMARY KEY AUTOINCREMENT,
    evento_id INTEGER NOT NULL,
    condicion_alarma INTEGER NOT NULL,
    fecha_hora_inicio_registro TEXT,
    fecha_hora_registro TEXT,
    frecuencia_muestreo INTEGER NOT NULL,
    FOREIGN KEY (evento_id) REFERENCES evento_sismico(id_evento)
);


-- Tabla TipoDeDato
CREATE TABLE tipo_de_dato (
    id_tipo_de_dato INTEGER PRIMARY KEY AUTOINCREMENT,
    denominacion VARCHAR(100) NOT NULL,
    nombre_unidad_medida VARCHAR(50),
    valor_umbral DOUBLE
);

-- Tabla MuestraSismica
CREATE TABLE muestra_sismica (
    id_muestra INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_hora_muestra TEXT NOT NULL,
    id_serie INTEGER NOT NULL,
    FOREIGN KEY (id_serie) REFERENCES serie_temporal(id_serie)
);

-- Tabla DetalleMuestraSismica
CREATE TABLE detalle_muestra_sismica (
    id_detalle INTEGER PRIMARY KEY AUTOINCREMENT,
    id_muestra INTEGER NOT NULL,
    valor DOUBLE NOT NULL,
    id_tipo_de_dato INTEGER NOT NULL,
    FOREIGN KEY (id_muestra) REFERENCES muestra_sismica(id_muestra),
    FOREIGN KEY (id_tipo_de_dato) REFERENCES tipo_de_dato(id_tipo_de_dato)
);


