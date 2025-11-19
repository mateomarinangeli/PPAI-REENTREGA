package com.redsismica.demo.persistence.EstacionSismologica;

import com.redsismica.demo.domain.EstacionSismologica;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDREstacionSismologica implements IIntermediarioBDREstacionSismologica {

    private final String dbUrl;

    public IntermediarioBDREstacionSismologica(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    // Asumimos que EstacionSismologica tiene setters/getters, incluyendo codigoEstacion.
    private EstacionSismologica mapResultSet(ResultSet rs) throws SQLException {
        EstacionSismologica est = new EstacionSismologica();
        est.setCodigoEstacion(rs.getString("codigo_estacion"));
        est.setNombre(rs.getString("nombre"));
        est.setLatitud(rs.getDouble("latitud"));
        est.setLongitud(rs.getDouble("longitud"));
        est.setNroCertificacionAdquisicion(rs.getInt("nro_certificacion_adquisicion"));

        String fechaCertificacionTexto = rs.getString("fecha_solicitud_certificacion");

        if (fechaCertificacionTexto != null) {
            // Asume que la cadena está en formato ISO 8601 (el formato estándar de LocalDateTime.toString())
            est.setFechaSolicitudCertificacion(LocalDateTime.parse(fechaCertificacionTexto));
        } else {
            est.setFechaSolicitudCertificacion(null);
        }

        est.setNroCertificacionAdquisicion(rs.getInt("nro_certificacion_adquisicion"));
        
        return est;
    }

    @Override
    public EstacionSismologica findById(String codigoEstacion) {
        // Uso de PK de tipo TEXT
        String sql = "SELECT * FROM estacion_sismologica WHERE codigo_estacion = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoEstacion); // Se usa setString
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar EstacionSismologica por código: " + codigoEstacion, e);
        }
        return null;
    }

    @Override
    public List<EstacionSismologica> findAll() {
        String sql = "SELECT * FROM estacion_sismologica";
        List<EstacionSismologica> estaciones = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                estaciones.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todas las EstacionSismologica.", e);
        }
        return estaciones;
    }

    @Override
    public void insert(EstacionSismologica estacion) {
        String sql = "INSERT INTO estacion_sismologica (codigo_estacion, nombre, latitud, longitud, fecha_solicitud_certificacion, nro_certificacion_adquisicion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estacion.getCodigoEstacion());
            stmt.setString(2, estacion.getNombre());
            stmt.setDouble(3, estacion.getLatitud());
            stmt.setDouble(4, estacion.getLongitud());
            stmt.setString(5, estacion.getFechaSolicitudCertificacion().toString());
            stmt.setInt(6, estacion.getNroCertificacionAdquisicion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar EstacionSismologica: " + estacion.getCodigoEstacion(), e);
        }
    }

    @Override
    public void update(EstacionSismologica estacion) {
        String sql = "UPDATE estacion_sismologica SET nombre = ?, latitud = ?, longitud = ?, fecha_solicitud_certificacion = ?, nro_certificacion_adquisicion = ? WHERE codigo_estacion = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estacion.getNombre());
            stmt.setDouble(2, estacion.getLatitud());
            stmt.setDouble(3, estacion.getLongitud());
            stmt.setString(4, estacion.getFechaSolicitudCertificacion().toString());
            stmt.setInt(5, estacion.getNroCertificacionAdquisicion());
            // Usamos el codigo_estacion para la cláusula WHERE
            stmt.setString(6, estacion.getCodigoEstacion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar EstacionSismologica: " + estacion.getCodigoEstacion(), e);
        }
    }
}