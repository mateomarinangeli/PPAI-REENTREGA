package com.redsismica.demo.persistence.MagnitudRichter;

import com.redsismica.demo.domain.MagnitudRichter;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRMagnitudRichter implements IIntermediarioBDRMagnitudRichter {

    private final String dbUrl;

    public IntermediarioBDRMagnitudRichter(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private MagnitudRichter mapResultSet(ResultSet rs) throws SQLException {
        MagnitudRichter magnitud = new MagnitudRichter();
        magnitud.setIdMagnitud(rs.getInt("id_magnitud"));
        magnitud.setNumero(rs.getDouble("numero"));
        magnitud.setDescripcionMagnitud(rs.getString("descripcion_magnitud"));
        return magnitud;
    }

    @Override
    public MagnitudRichter findById(int id) {
        String sql = "SELECT * FROM magnitud_richter WHERE id_magnitud = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar MagnitudRichter por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<MagnitudRichter> findAll() {
        String sql = "SELECT * FROM magnitud_richter";
        List<MagnitudRichter> magnitudes = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                magnitudes.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todas las Magnitudes Richter.", e);
        }
        return magnitudes;
    }

    @Override
    public void insert(MagnitudRichter magnitud) {
        String sql = "INSERT INTO magnitud_richter (numero, descripcion_magnitud) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, magnitud.getNumero());
            stmt.setString(2, magnitud.getDescripcionMagnitud());
            stmt.executeUpdate();

            // Recuperar el ID y asignarlo al objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    magnitud.setIdMagnitud(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar MagnitudRichter.", e);
        }
    }

    @Override
    public void update(MagnitudRichter magnitud) {
        String sql = "UPDATE magnitud_richter SET numero = ?, descripcion_magnitud = ? WHERE id_magnitud = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, magnitud.getNumero());
            stmt.setString(2, magnitud.getDescripcionMagnitud());
            stmt.setInt(3, magnitud.getIdMagnitud());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar MagnitudRichter ID: " + magnitud.getIdMagnitud(), e);
        }
    }
}