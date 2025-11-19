package com.redsismica.demo.persistence.DetalleMuestraSismica;

import com.redsismica.demo.domain.DetalleMuestraSismica;
import com.redsismica.demo.domain.TipoDeDato;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRDetalleMuestraSismica implements IIntermediarioBDRDetalleMuestraSismica {

    private final String dbUrl;

    public IntermediarioBDRDetalleMuestraSismica(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private DetalleMuestraSismica mapResultSet(ResultSet rs) throws SQLException {
        DetalleMuestraSismica detalle = new DetalleMuestraSismica();
        detalle.setValor(rs.getDouble("valor"));

        TipoDeDato tipo = new TipoDeDato();
        tipo.setDenominacion(rs.getString("denominacion"));
        tipo.setNombreUnidadMedida(rs.getString("nombre_unidad_medida"));
        tipo.setValorUmbral(rs.getDouble("valor_umbral"));
        // Asignamos el tipo al detalle
        detalle.setTipoDeDato(tipo);

        return detalle;
    }

    @Override
    public DetalleMuestraSismica findById(int id) {
        String sql = "SELECT d.*, t.denominacion, t.nombre_unidad_medida, t.valor_umbral " +
                "FROM detalle_muestra_sismica d " +
                "JOIN tipo_de_dato t ON d.id_tipo_de_dato = t.id_tipo_de_dato " +
                "WHERE d.id_detalle = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DetalleMuestraSismica por ID.", e);
        }
        return null;
    }

    @Override
    public List<DetalleMuestraSismica> findAll() {
        String sql = "SELECT d.*, t.denominacion, t.nombre_unidad_medida, t.valor_umbral " +
                "FROM detalle_muestra_sismica d " +
                "JOIN tipo_de_dato t ON d.id_tipo_de_dato = t.id_tipo_de_dato";
        List<DetalleMuestraSismica> detalles = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                detalles.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los DetalleMuestraSismica.", e);
        }
        return detalles;
    }

    @Override
    public List<DetalleMuestraSismica> findAllByMuestraId(int idMuestra) {
        String sql = "SELECT d.*, t.denominacion, t.nombre_unidad_medida, t.valor_umbral " +
                "FROM detalle_muestra_sismica d " +
                "JOIN tipo_de_dato t ON d.id_tipo_de_dato = t.id_tipo_de_dato " +
                "WHERE d.id_muestra = ?";
        List<DetalleMuestraSismica> detalles = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMuestra);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DetalleMuestraSismica por Muestra ID " + idMuestra, e);
        }
        return detalles;
    }

    @Override
    public void insert(DetalleMuestraSismica detalle, int idMuestra) {
        String sql = "INSERT INTO detalle_muestra_sismica (id_muestra, valor, id_tipo_de_dato) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idMuestra);
            stmt.setDouble(2, detalle.getValor());
            stmt.setInt(3, detalle.getTipoDeDato().getIdTipoDeDato());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // detalle.setIdDetalle(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar DetalleMuestraSismica para Muestra ID: " + idMuestra, e);
        }
    }
}
