package com.redsismica.demo.persistence.CambioEstado;

import com.redsismica.demo.domain.state.CambioEstado;
import com.redsismica.demo.persistence.EventoSismico.IIntermediarioBDREventoSismico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import com.redsismica.demo.persistence.Empleado.IIntermediarioBDREmpleado;
import com.redsismica.demo.persistence.Estado.IIntermediarioBDREstado;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRCambioEstado implements IIntermediarioBDRCambioEstado {

    private final String dbUrl;
    // 🛑 Inyección de Mappers para resolver las referencias
    private final IIntermediarioBDREmpleado empleadoMapper;
    private final IIntermediarioBDREstado estadoMapper;
    // private final IIntermediarioBDREventoSismico eventoMapper;

    @Lazy
    private IIntermediarioBDREventoSismico eventoMapper; // ahora NO va en constructor

    // Constructor con inyección de dependencias
    public IntermediarioBDRCambioEstado(
            @Value("${spring.datasource.url}") String dbUrl,
            IIntermediarioBDREmpleado empleadoMapper,
            IIntermediarioBDREstado estadoMapper) {

        this.dbUrl = dbUrl;
        this.empleadoMapper = empleadoMapper;
        this.estadoMapper = estadoMapper;
        // this.eventoMapper = eventoMapper;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    @Autowired
    public void setEventoMapper(@Lazy IIntermediarioBDREventoSismico eventoMapper) {
        this.eventoMapper = eventoMapper;
    }

    /**
     * Mapea un registro de ResultSet a un objeto CambioEstado,
     * utilizando los Mappers inyectados para resolver las FKs.
     */
    private CambioEstado mapResultSet(ResultSet rs) throws SQLException {
        CambioEstado cambio = new CambioEstado();

        // 1. Mapeo del ID propio
        cambio.setIdCambioEstado(rs.getInt("id_cambio_estado"));

        // 2. Mapeo de LocalDateTime (String/TEXT -> LocalDateTime)
        String fechaInicioTexto = rs.getString("fecha_hora_inicio");
        String fechaFinTexto = rs.getString("fecha_hora_fin");

        if (fechaInicioTexto != null) {
            cambio.setFechaHoraInicio(LocalDateTime.parse(fechaInicioTexto));
        }
        if (fechaFinTexto != null) {
            cambio.setFechaHoraFin(LocalDateTime.parse(fechaFinTexto));
        }

        // 3. 🛑 Mapeo de referencias (Ensamblador)

        int estadoId = rs.getInt("estado_id");
        if (!rs.wasNull()) {
            // Se usa findById del mapper de Estado, que resuelve la clase concreta (State Pattern)
            cambio.setEstado(estadoMapper.findById(estadoId));
        }

        int empleadoId = rs.getInt("empleado_id");
        if (!rs.wasNull()) {
            cambio.setEmpleado(empleadoMapper.findById(empleadoId));
        }

        return cambio;
    }

    @Override
    public CambioEstado findById(int id) {
        String sql = "SELECT * FROM cambio_estado WHERE id_cambio_estado = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar CambioEstado por ID.", e);
        }
        return null;
    }

    @Override
    public List<CambioEstado> findAll() {
        String sql = "SELECT * FROM cambio_estado";
        List<CambioEstado> cambios = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cambios.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los CambioEstado.", e);
        }
        return cambios;
    }

    @Override
    public List<CambioEstado> findAllByEventoId(int eventoId) {
        // Busca el historial y lo ordena por fecha/hora
        String sql = "SELECT * FROM cambio_estado WHERE evento_id = ? ORDER BY fecha_hora_inicio ASC";
        List<CambioEstado> historial = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historial.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar historial de CambioEstado para evento ID " + eventoId, e);
        }
        return historial;
    }

    @Override
    public void insert(CambioEstado cambio, int idEvento) {
        String sql = "INSERT INTO cambio_estado (evento_id, estado_id, empleado_id, fecha_hora_inicio, fecha_hora_fin) VALUES (?, ?, ?, ?, ?)";

        // 🛑 Desensamblaje: Obtener los IDs de los objetos referenciados
        // 1. Usamos el idEvento pasado por parámetro.
        // 2. Obtenemos el ID de Empleado del objeto.
        int empleadoId = cambio.getEmpleado().getIdEmpleado();

        // 3. Caso especial para Estado (sin ID en el objeto), se busca el ID por nombre.
        // *Requiere que IIntermediarioBDREstado.findIdByNombre() exista.*
        int estadoId = estadoMapper.findIdByNombre(cambio.getEstado().getNombre());

        // Conversión de LocalDateTime a String/TEXT
        String fechaInicioTexto = cambio.getFechaHoraInicio() != null ? cambio.getFechaHoraInicio().toString() : null;
        String fechaFinTexto = cambio.getFechaHoraFin() != null ? cambio.getFechaHoraFin().toString() : null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 🛑 Usamos el parámetro idEvento para la primera FK
            stmt.setInt(1, idEvento);
            stmt.setInt(2, estadoId);
            stmt.setInt(3, empleadoId);
            stmt.setString(4, fechaInicioTexto);
            stmt.setString(5, fechaFinTexto);

            stmt.executeUpdate();

            // Recuperar el ID propio del historial
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cambio.setIdCambioEstado(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar CambioEstado para Evento ID: " + idEvento, e);
        }
    }

    @Override
    public void update(CambioEstado cambio) {
        String sql = "UPDATE cambio_estado " +
                "SET estado_id = ?, empleado_id = ?, fecha_hora_inicio = ?, fecha_hora_fin = ? " +
                "WHERE id_cambio_estado = ?";

        // 🛑 Igual que en insert: obtenemos IDs de las referencias
        int empleadoId = cambio.getEmpleado().getIdEmpleado();
        int estadoId = estadoMapper.findIdByNombre(cambio.getEstado().getNombre());

        String fechaInicioTexto =
                cambio.getFechaHoraInicio() != null ? cambio.getFechaHoraInicio().toString() : null;

        String fechaFinTexto =
                cambio.getFechaHoraFin() != null ? cambio.getFechaHoraFin().toString() : null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estadoId);
            stmt.setInt(2, empleadoId);
            stmt.setString(3, fechaInicioTexto);
            stmt.setString(4, fechaFinTexto);
            stmt.setInt(5, cambio.getIdCambioEstado());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al actualizar CambioEstado con ID " + cambio.getIdCambioEstado(), e
            );
        }
    }

}