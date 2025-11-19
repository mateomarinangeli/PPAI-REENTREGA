package com.redsismica.demo.persistence.AlcanceSismo;

import com.redsismica.demo.domain.AlcanceSismo;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRAlcanceSismo implements IIntermediarioBDRAlcanceSismo {

    private final String dbUrl;

    public IntermediarioBDRAlcanceSismo(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private AlcanceSismo mapResultSet(ResultSet rs) throws SQLException {
        AlcanceSismo alcance = new AlcanceSismo();
        // 🛑 Mapeo de atributos según la clase de dominio
        alcance.setIdAlcance(rs.getInt("id_alcance"));
        alcance.setNombre(rs.getString("nombre"));
        alcance.setDescripcion(rs.getString("descripcion"));
        return alcance;
    }

    @Override
    public AlcanceSismo findById(int id) {
        String sql = "SELECT * FROM alcance_sismo WHERE id_alcance = ?";
        // ... (Implementación de findById) ...
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar AlcanceSismo por ID.", e);
        }
        return null;
    }

    @Override
    public List<AlcanceSismo> findAll() {
        String sql = "SELECT * FROM alcance_sismo";
        List<AlcanceSismo> alcances = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                alcances.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los AlcanceSismo.", e);
        }
        return alcances;
    }

    @Override
    public void insert(AlcanceSismo alcance) {
        // 🛑 SQL corregido: Solo nombre y descripcion
        String sql = "INSERT INTO alcance_sismo (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = getConnection();
             // Solicitamos la clave generada
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, alcance.getNombre());
            stmt.setString(2, alcance.getDescripcion());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    alcance.setIdAlcance(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar AlcanceSismo.", e);
        }
    }

    @Override
    public void update(AlcanceSismo alcance) {
        // 🛑 SQL corregido: Solo nombre y descripcion, usando idAlcance para WHERE
        String sql = "UPDATE alcance_sismo SET nombre = ?, descripcion = ? WHERE id_alcance = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, alcance.getNombre());
            stmt.setString(2, alcance.getDescripcion());
            stmt.setInt(3, alcance.getIdAlcance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar AlcanceSismo.", e);
        }
    }
}