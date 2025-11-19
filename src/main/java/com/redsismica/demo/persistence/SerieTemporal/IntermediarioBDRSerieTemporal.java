package com.redsismica.demo.persistence.SerieTemporal;

import com.redsismica.demo.domain.SerieTemporal;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
// import com.redsismica.demo.persistence.EventoSismico.IIntermediarioBDREventoSismico;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRSerieTemporal implements IIntermediarioBDRSerieTemporal {

    private final String dbUrl;
    // Inyección del Mapper del objeto referenciado
    // private final IIntermediarioBDREventoSismico eventoMapper;

    public IntermediarioBDRSerieTemporal(
            @Value("${spring.datasource.url}") String dbUrl) {

        this.dbUrl = dbUrl;
        // this.eventoMapper = eventoMapper;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private SerieTemporal mapResultSet(ResultSet rs) throws SQLException {
        SerieTemporal serie = new SerieTemporal();

        serie.setIdSerieTemporal(rs.getInt("id_serie"));
        serie.setFrecuenciaMuestreo(rs.getInt("frecuencia_muestreo"));

        // 🛑 LECTURA BOOLEANA: int (DB) a boolean (Java)
        int condicionAlarmaDb = rs.getInt("condicion_alarma");
        serie.setCondicionAlarma(condicionAlarmaDb == 1); // 1 es true, 0 es false

        // Mapeo de LocalDateTime (conversión de String/TEXT a LocalDateTime)
        String fechaInicioTexto = rs.getString("fecha_hora_inicio_registro");
        String fechaRegistroTexto = rs.getString("fecha_hora_registro");

        if (fechaInicioTexto != null) {
            serie.setFechaHoraInicioRegistroMuestras(LocalDateTime.parse(fechaInicioTexto));
        }
        if (fechaRegistroTexto != null) {
            serie.setFechaHoraRegistro(LocalDateTime.parse(fechaRegistroTexto));
        }

        return serie;
    }

    @Override
    public SerieTemporal findById(int id) {
        String sql = "SELECT * FROM serie_temporal WHERE id_serie = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar SerieTemporal por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<SerieTemporal> findAllByEventoId(int eventoId) {
        String sql = "SELECT * FROM serie_temporal WHERE evento_id = ? ORDER BY fecha_hora_registro ASC";
        List<SerieTemporal> series = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    series.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar SeriesTemporales para evento ID " + eventoId, e);
        }
        return series;
    }

    @Override
    public void insert(SerieTemporal serie, int idEvento) {
        String sql = "INSERT INTO serie_temporal (evento_id, condicion_alarma, fecha_hora_inicio_registro, fecha_hora_registro, frecuencia_muestreo) VALUES (?, ?, ?, ?, ?)";

        // Conversión de LocalDateTime a String/TEXT
        String fechaInicioTexto = serie.getFechaHoraInicioRegistroMuestras() != null ? serie.getFechaHoraInicioRegistroMuestras().toString() : null;
        String fechaRegistroTexto = serie.getFechaHoraRegistro() != null ? serie.getFechaHoraRegistro().toString() : null;

        // 🛑 ESCRITURA BOOLEANA: boolean (Java) a int (DB)
        int condicionAlarmaDb = serie.isCondicionAlarma() ? 1 : 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idEvento); // ID del evento pasado por parámetro
            stmt.setInt(2, condicionAlarmaDb);
            stmt.setString(3, fechaInicioTexto);
            stmt.setString(4, fechaRegistroTexto);
            stmt.setInt(5, serie.getFrecuenciaMuestreo());

            stmt.executeUpdate();

            // Recuperar el ID propio
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    serie.setIdSerieTemporal(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar SerieTemporal para Evento ID: " + idEvento, e);
        }
    }

    // Omitiendo update() y findAll() por ser menos comunes para tablas de series temporales/historial.
}