package com.redsismica.demo.persistence.TipoDeDato;

import com.redsismica.demo.domain.TipoDeDato;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRTipoDeDato implements IIntermediarioBDRTipoDeDato {

    private final String dbUrl;

    public IntermediarioBDRTipoDeDato(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private TipoDeDato mapResultSet(ResultSet rs) throws SQLException {
        TipoDeDato tipo = new TipoDeDato();
        tipo.setIdTipoDeDato(rs.getInt("id_tipo_de_dato"));
        tipo.setDenominacion(rs.getString("denominacion"));
        tipo.setNombreUnidadMedida(rs.getString("nombre_unidad_medida"));
        tipo.setValorUmbral(rs.getDouble("valor_umbral"));
        return tipo;
    }

    @Override
    public TipoDeDato findById(int id) {
        String sql = "SELECT * FROM tipo_de_dato WHERE id_tipo_de_dato = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar TipoDeDato por ID.", e);
        }
        return null;
    }

    @Override
    public List<TipoDeDato> findAll() {
        String sql = "SELECT * FROM tipo_de_dato";
        List<TipoDeDato> tipos = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tipos.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los TipoDeDato.", e);
        }
        return tipos;
    }

    @Override
    public void insert(TipoDeDato tipo) {
        String sql = "INSERT INTO tipo_de_dato (denominacion, nombre_unidad_medida, valor_umbral) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tipo.getDenominacion());
            stmt.setString(2, tipo.getNombreUnidadMedida());
            stmt.setDouble(3, tipo.getValorUmbral());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tipo.setIdTipoDeDato(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar TipoDeDato.", e);
        }
    }
}
