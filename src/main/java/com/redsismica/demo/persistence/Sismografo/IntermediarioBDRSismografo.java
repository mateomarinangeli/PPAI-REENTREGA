package com.redsismica.demo.persistence.Sismografo;

import com.redsismica.demo.domain.Sismografo;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import com.redsismica.demo.persistence.EstacionSismologica.IIntermediarioBDREstacionSismologica;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRSismografo implements IIntermediarioBDRSismografo {

    private final String dbUrl;
    private final IIntermediarioBDREstacionSismologica estacionMapper;

    public IntermediarioBDRSismografo(@Value("${spring.datasource.url}") String dbUrl,
                                          IIntermediarioBDREstacionSismologica estacionMapper) {
        this.dbUrl = dbUrl;
        this.estacionMapper = estacionMapper;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private Sismografo mapResultSet(ResultSet rs) throws SQLException {
        Sismografo sismografo = new Sismografo();
        sismografo.setIdentificadorSismografo(rs.getInt("identificador_sismografo"));
        sismografo.setNroSerie(rs.getInt("nro_serie"));

        // Mapeo de LocalDateTime
        String fechaAdquisicionTexto = rs.getString("fecha_adquisicion");
        if (fechaAdquisicionTexto != null) {
            sismografo.setFechaAdquisicion(LocalDateTime.parse(fechaAdquisicionTexto));
        }

        // Ensamblaje de la referencia EstacionSismologica (PK es TEXT)
        String codigoEstacion = rs.getString("codigo_estacion");
        if (codigoEstacion != null) {
            sismografo.setEstacionSismologica(estacionMapper.findById(codigoEstacion));
        }

        return sismografo;
    }

    @Override
    public Sismografo findById(int id) {
        String sql = "SELECT * FROM sismografo WHERE identificador_sismografo = ?";
        // ... (Implementación de findById) ...
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Sismografo por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Sismografo> findAllByCodigoEstacion(String codigoEstacion) {
        String sql = "SELECT * FROM sismografo WHERE codigo_estacion = ?";
        List<Sismografo> sismografos = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoEstacion);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) sismografos.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Sismografos por código de estación: " + codigoEstacion, e);
        }
        return sismografos;
    }

    @Override
    public void insert(Sismografo sismografo) {
        String sql = "INSERT INTO sismografo (fecha_adquisicion, nro_serie, codigo_estacion) VALUES (?, ?, ?)";

        String fechaAdquisicionTexto = sismografo.getFechaAdquisicion() != null ?
                sismografo.getFechaAdquisicion().toString() :
                null;
        String codigoEstacion = sismografo.getEstacionSismologica().getCodigoEstacion();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, fechaAdquisicionTexto);
            stmt.setInt(2, sismografo.getNroSerie());
            stmt.setString(3, codigoEstacion);
            stmt.executeUpdate();

            // La PK es identificador_sismografo (INT), si es autoincremental
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) sismografo.setIdentificadorSismografo(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar Sismografo.", e);
        }
    }

    @Override
    public List<Sismografo> findAll() {
        String sql = "SELECT * FROM sismografo";
        List<Sismografo> sismografos = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Usamos el método de mapeo para ensamblar el objeto
                sismografos.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los Sismografos.", e);
        }
        return sismografos;
    }

    /*
     * Implementación del método update(Sismografo)
     */
    @Override
    public void update(Sismografo sismografo) {
        String sql = "UPDATE sismografo SET fecha_adquisicion = ?, nro_serie = ?, codigo_estacion = ? WHERE identificador_sismografo = ?";

        // 1. Desensamblaje: Conversión de LocalDateTime a String/TEXT
        String fechaAdquisicionTexto = sismografo.getFechaAdquisicion() != null ?
                sismografo.getFechaAdquisicion().toString() :
                null;

        // 2. Desensamblaje: Obtener el ID de la FK (código de estación)
        String codigoEstacion = sismografo.getEstacionSismologica().getCodigoEstacion();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fechaAdquisicionTexto); // fecha_adquisicion
            stmt.setInt(2, sismografo.getNroSerie());     // nro_serie
            stmt.setString(3, codigoEstacion);            // codigo_estacion

            // Cláusula WHERE: Usamos el ID propio del sismógrafo
            stmt.setInt(4, sismografo.getIdentificadorSismografo()); // identificador_sismografo

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar Sismografo ID: " + sismografo.getIdentificadorSismografo(), e);
        }
    }

}