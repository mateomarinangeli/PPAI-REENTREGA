package com.redsismica.demo.persistence.OrigenDeGeneracion;

import com.redsismica.demo.domain.OrigenDeGeneracion;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDROrigenDeGeneracion implements IIntermediarioBDROrigenGeneracion {

    private final String dbUrl;

    public IntermediarioBDROrigenDeGeneracion(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private OrigenDeGeneracion mapResultSet(ResultSet rs) throws SQLException {
        OrigenDeGeneracion origen = new OrigenDeGeneracion();
        origen.setIdOrigen(rs.getInt("id_origen"));
        origen.setNombre(rs.getString("nombre"));
        origen.setDescripcion(rs.getString("descripcion"));
        return origen;
    }

    @Override
    public OrigenDeGeneracion findById(int id) {
        String sql = "SELECT * FROM origen_generacion WHERE id_origen = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar OrigenGeneracion por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<OrigenDeGeneracion> findAll() {
        String sql = "SELECT * FROM origen_generacion";
        List<OrigenDeGeneracion> origenes = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                origenes.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los OrigenGeneracion.", e);
        }
        return origenes;
    }

    @Override
    public void insert(OrigenDeGeneracion origen) {
        String sql = "INSERT INTO origen_generacion (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, origen.getNombre());
            stmt.setString(2, origen.getDescripcion());
            stmt.executeUpdate();

            // Recuperar el ID y asignarlo al objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    origen.setIdOrigen(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar OrigenGeneracion.", e);
        }
    }

    @Override
    public void update(OrigenDeGeneracion origen) {
        String sql = "UPDATE origen_generacion SET nombre = ?, descripcion = ? WHERE id_origen = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, origen.getNombre());
            stmt.setString(2, origen.getDescripcion());
            stmt.setInt(3, origen.getIdOrigen());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar OrigenGeneracion ID: " + origen.getIdOrigen(), e);
        }
    }
}