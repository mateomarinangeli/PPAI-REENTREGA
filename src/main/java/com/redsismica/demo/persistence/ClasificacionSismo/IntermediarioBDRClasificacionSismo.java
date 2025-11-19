package com.redsismica.demo.persistence.ClasificacionSismo;

import com.redsismica.demo.domain.ClasificacionSismo;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRClasificacionSismo implements IIntermediarioBDRClasificacionSismo {

    private final String dbUrl;

    public IntermediarioBDRClasificacionSismo(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private ClasificacionSismo mapResultSet(ResultSet rs) throws SQLException {
        ClasificacionSismo clasificacion = new ClasificacionSismo();
        clasificacion.setIdClasificacion(rs.getInt("id_clasificacion"));
        // 🛑 Uso de getters/setters según la clase de dominio
        clasificacion.setKmProfundidadDesde(rs.getInt("km_profundidad_desde"));
        clasificacion.setKmProfundidadHasta(rs.getInt("km_profundidad_hasta"));
        clasificacion.setNombre(rs.getString("nombre"));
        return clasificacion;
    }

    @Override
    public ClasificacionSismo findById(int id) {
        String sql = "SELECT * FROM clasificacion_sismo WHERE id_clasificacion = ?";
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
            throw new RuntimeException("Error al buscar ClasificacionSismo por ID.", e);
        }
        return null;
    }

    @Override
    public List<ClasificacionSismo> findAll() {
        String sql = "SELECT * FROM clasificacion_sismo";
        List<ClasificacionSismo> clasificaciones = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clasificaciones.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todas las Clasificaciones Sismo.", e);
        }
        return clasificaciones;
    }

    @Override
    public void insert(ClasificacionSismo clasificacion) {
        String sql = "INSERT INTO clasificacion_sismo (km_profundidad_desde, km_profundidad_hasta, nombre) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 🛑 Uso de getters/setters según la clase de dominio
            stmt.setInt(1, clasificacion.getKmProfundidadDesde());
            stmt.setInt(2, clasificacion.getKmProfundidadHasta());
            stmt.setString(3, clasificacion.getNombre());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    clasificacion.setIdClasificacion(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar ClasificacionSismo.", e);
        }
    }

    @Override
    public void update(ClasificacionSismo clasificacion) {
        String sql = "UPDATE clasificacion_sismo SET km_profundidad_desde = ?, km_profundidad_hasta = ?, nombre = ? WHERE id_clasificacion = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 🛑 Uso de getters/setters según la clase de dominio
            stmt.setInt(1, clasificacion.getKmProfundidadDesde());
            stmt.setInt(2, clasificacion.getKmProfundidadHasta());
            stmt.setString(3, clasificacion.getNombre());
            stmt.setInt(4, clasificacion.getIdClasificacion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar ClasificacionSismo.", e);
        }
    }
}