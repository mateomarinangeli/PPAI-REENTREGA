package com.redsismica.demo.persistence.EventoSismico;

import com.redsismica.demo.domain.EventoSismico;
import com.redsismica.demo.domain.SerieTemporal;
import com.redsismica.demo.domain.state.CambioEstado;
import com.redsismica.demo.persistence.CambioEstado.IIntermediarioBDRCambioEstado;
import com.redsismica.demo.persistence.SerieTemporal.IIntermediarioBDRSerieTemporal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import com.redsismica.demo.persistence.AlcanceSismo.IIntermediarioBDRAlcanceSismo;
import com.redsismica.demo.persistence.ClasificacionSismo.IIntermediarioBDRClasificacionSismo;
import com.redsismica.demo.persistence.Empleado.IIntermediarioBDREmpleado;
import com.redsismica.demo.persistence.Estado.IIntermediarioBDREstado;
import com.redsismica.demo.persistence.MagnitudRichter.IIntermediarioBDRMagnitudRichter;
import com.redsismica.demo.persistence.OrigenDeGeneracion.IIntermediarioBDROrigenGeneracion;
import com.redsismica.demo.persistence.SerieTemporal.IIntermediarioBDRSerieTemporal;
import com.redsismica.demo.persistence.CambioEstado.IIntermediarioBDRCambioEstado;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDREventoSismico implements IIntermediarioBDREventoSismico {

    private final String dbUrl;
    // Inyección de todos los Mappers dependientes
    private final IIntermediarioBDREmpleado empleadoMapper;
    private final IIntermediarioBDREstado estadoMapper;
    private final IIntermediarioBDRAlcanceSismo alcanceMapper;
    private final IIntermediarioBDROrigenGeneracion origenMapper;
    private final IIntermediarioBDRMagnitudRichter magnitudMapper;
    private final IIntermediarioBDRClasificacionSismo clasificacionMapper;
    private final IIntermediarioBDRSerieTemporal serieTemporalMapper;

    @Lazy
    private IIntermediarioBDRCambioEstado cambioEstadoMapper;

    // Constructor que inyecta la URL y todos los Mappers
    public IntermediarioBDREventoSismico(
            @Value("${spring.datasource.url}") String dbUrl,
            IIntermediarioBDREmpleado empleadoMapper,
            IIntermediarioBDREstado estadoMapper,
            IIntermediarioBDRAlcanceSismo alcanceMapper,
            IIntermediarioBDROrigenGeneracion origenMapper,
            IIntermediarioBDRMagnitudRichter magnitudMapper,
            IIntermediarioBDRClasificacionSismo clasificacionMapper,
            IIntermediarioBDRSerieTemporal serieTemporalMapper) {

        this.dbUrl = dbUrl;
        this.empleadoMapper = empleadoMapper;
        this.estadoMapper = estadoMapper;
        this.alcanceMapper = alcanceMapper;
        this.origenMapper = origenMapper;
        this.magnitudMapper = magnitudMapper;
        this.clasificacionMapper = clasificacionMapper;
        this.serieTemporalMapper = serieTemporalMapper;
        // this.cambioEstadoMapper = cambioEstadoMapper;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    @Autowired
    public void setCambioEstadoMapper(@Lazy IIntermediarioBDRCambioEstado cambioMapper) {
        this.cambioEstadoMapper = cambioMapper;
    }

    /**
     * Ensambla el objeto EventoSismico a partir de un ResultSet, resolviendo las FKs.
     */
    private EventoSismico mapResultSet(ResultSet rs) throws SQLException {
        EventoSismico evento = new EventoSismico();

        evento.setIdEvento(rs.getInt("id_evento"));

        // Mapeo de LocalDateTime
        String fechaOcurrenciaTexto = rs.getString("fecha_hora_ocurrencia");
        String fechaFinTexto = rs.getString("fecha_hora_fin");

        if (fechaOcurrenciaTexto != null) {
            evento.setFechaHoraOcurrencia(LocalDateTime.parse(fechaOcurrenciaTexto));
        }
        if (fechaFinTexto != null) {
            evento.setFechaHoraFin(LocalDateTime.parse(fechaFinTexto));
        }

        evento.setLatitudEpicentro(rs.getDouble("latitud_epicentro"));
        evento.setLongitudEpicentro(rs.getDouble("longitud_epicentro"));
        evento.setLatitudHipocentro(rs.getDouble("latitud_hipocentro"));
        evento.setLongitudHipocentro(rs.getDouble("longitud_hipocentro"));
        evento.setValorMagnitud(rs.getDouble("valor_magnitud"));

        // 🛑 Ensamblaje de referencias (punteros): Usamos IDs de BD para buscar objetos

        // 1. Analista Supervisor (Empleado)
        int analistaId = rs.getInt("analista_supervisor_id");
        if (!rs.wasNull()) {
            evento.setAnalistaSupervisor(empleadoMapper.findById(analistaId));
        }

        // 2. Estado Actual (Estado - Patrón State)
        int estadoId = rs.getInt("estado_actual_id");
        if (!rs.wasNull()) {
            evento.setEstadoActual(estadoMapper.findById(estadoId));
        }

        // 3. Alcance Sismo
        int alcanceId = rs.getInt("alcance_id");
        if (!rs.wasNull()) {
            evento.setAlcanceSismo(alcanceMapper.findById(alcanceId));
        }

        // 4. Origen Generacion
        int origenId = rs.getInt("origen_id");
        if (!rs.wasNull()) {
            evento.setOrigenGeneracion(origenMapper.findById(origenId));
        }

        // 5. Magnitud Richter
        int magnitudId = rs.getInt("id_magnitud");
        if (!rs.wasNull()) {
            evento.setMagnitud(magnitudMapper.findById(magnitudId));
        }

        // 6. Clasificación Sismo
        int clasificacionId = rs.getInt("clasificacion_id");
        if (!rs.wasNull()) {
            evento.setClasificacion(clasificacionMapper.findById(clasificacionId));
        }

        List<SerieTemporal> seriesDeEvento = serieTemporalMapper.findAllByEventoId(rs.getInt("id_evento"));
        evento.setSerieTemporal(seriesDeEvento);

        List<CambioEstado> cambiosEstadoEvento = cambioEstadoMapper.findAllByEventoId(rs.getInt("id_evento"));
        evento.setCambioEstado(cambiosEstadoEvento);

        return evento;
    }

    @Override
    public EventoSismico findById(int id) {
        String sql = "SELECT * FROM evento_sismico WHERE id_evento = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar EventoSismico por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<EventoSismico> findAll() {
        String sql = "SELECT * FROM evento_sismico";
        List<EventoSismico> eventos = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                eventos.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los EventoSismico.", e);
        }
        return eventos;
    }

    // --- INSERT ---
    @Override
    public void insert(EventoSismico evento) {
        String sql = "INSERT INTO evento_sismico (fecha_hora_ocurrencia, fecha_hora_fin, latitud_epicentro, longitud_epicentro, latitud_hipocentro, longitud_hipocentro, valor_magnitud, analista_supervisor_id, estado_actual_id, alcance_id, origen_id, id_magnitud, clasificacion_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Conversión de LocalDateTime a String/TEXT
        String fechaOcurrenciaTexto = evento.getFechaHoraOcurrencia() != null ? evento.getFechaHoraOcurrencia().toString() : null;
        String fechaFinTexto = evento.getFechaHoraFin() != null ? evento.getFechaHoraFin().toString() : null;

        // 🛑 Desensamblaje: Obtener IDs de los objetos referenciados para las FKs

        // Asumimos que Empleado, Alcance, etc., tienen getID...()
        int analistaId = evento.getAnalistaSupervisor().getIdEmpleado();
        int alcanceId = evento.getAlcanceSismo().getIdAlcance();
        int origenId = evento.getOrigenGeneracion().getIdOrigen(); // Asumiendo que el dominio Origen tiene idOrigen
        int magnitudId = evento.getMagnitud().getIdMagnitud();
        int clasificacionId = evento.getClasificacion().getIdClasificacion();

        // Caso especial Estado (sin ID en el objeto): Obtenemos el ID por el nombre del objeto concreto.
        int estadoId = estadoMapper.findIdByNombre(evento.getEstadoActual().getNombre());

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, fechaOcurrenciaTexto);
            stmt.setString(2, fechaFinTexto);
            stmt.setDouble(3, evento.getLatitudEpicentro());
            stmt.setDouble(4, evento.getLongitudEpicentro());
            stmt.setDouble(5, evento.getLatitudHipocentro());
            stmt.setDouble(6, evento.getLongitudHipocentro());
            stmt.setDouble(7, evento.getValorMagnitud());

            // 🛑 Setear las FKs con los IDs recuperados
            stmt.setInt(8, analistaId);
            stmt.setInt(9, estadoId);
            stmt.setInt(10, alcanceId);
            stmt.setInt(11, origenId);
            stmt.setInt(12, magnitudId);
            stmt.setInt(13, clasificacionId);

            stmt.executeUpdate();

            // Recuperar el ID y asignarlo al objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evento.setIdEvento(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar EventoSismico.", e);
        }
    }

    // --- UPDATE ---
    @Override
    public void update(EventoSismico evento) {
        String sql = "UPDATE evento_sismico SET fecha_hora_ocurrencia=?, fecha_hora_fin=?, latitud_epicentro=?, longitud_epicentro=?, latitud_hipocentro=?, longitud_hipocentro=?, valor_magnitud=?, analista_supervisor_id=?, estado_actual_id=?, alcance_id=?, origen_id=?, id_magnitud=?, clasificacion_id=? WHERE id_evento = ?";

        // Conversión de LocalDateTime a String/TEXT
        String fechaOcurrenciaTexto = evento.getFechaHoraOcurrencia() != null ? evento.getFechaHoraOcurrencia().toString() : null;
        String fechaFinTexto = evento.getFechaHoraFin() != null ? evento.getFechaHoraFin().toString() : null;

        // 🛑 Desensamblaje para UPDATE
        int analistaId = evento.getAnalistaSupervisor().getIdEmpleado();
        int estadoId = estadoMapper.findIdByNombre(evento.getEstadoActual().getNombre());
        int alcanceId = evento.getAlcanceSismo().getIdAlcance();
        int origenId = evento.getOrigenGeneracion().getIdOrigen();
        int magnitudId = evento.getMagnitud().getIdMagnitud();
        int clasificacionId = evento.getClasificacion().getIdClasificacion();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fechaOcurrenciaTexto);
            stmt.setString(2, fechaFinTexto);
            stmt.setDouble(3, evento.getLatitudEpicentro());
            stmt.setDouble(4, evento.getLongitudEpicentro());
            stmt.setDouble(5, evento.getLatitudHipocentro());
            stmt.setDouble(6, evento.getLongitudHipocentro());
            stmt.setDouble(7, evento.getValorMagnitud());

            // Setear las FKs
            stmt.setInt(8, analistaId);
            stmt.setInt(9, estadoId);
            stmt.setInt(10, alcanceId);
            stmt.setInt(11, origenId);
            stmt.setInt(12, magnitudId);
            stmt.setInt(13, clasificacionId);

            // Cláusula WHERE usando el ID del objeto
            stmt.setInt(14, evento.getIdEvento());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar EventoSismico ID: " + evento.getIdEvento(), e);
        }
    }
}