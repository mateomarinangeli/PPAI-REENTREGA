package com.redsismica.demo.persistence.Estado;

import com.redsismica.demo.domain.state.*;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDREstado implements IIntermediarioBDREstado {

    private final String dbUrl;

    public IntermediarioBDREstado(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    /**
     * Actúa como una Fábrica: lee el nombre del estado de la BD
     * y devuelve la instancia de la clase concreta (sin ID en el objeto).
     */
    private Estado mapResultSet(ResultSet rs) throws SQLException {
        String nombreEstado = rs.getString("nombre");

        switch (nombreEstado) {
            case "AutoDetectado":
                return new AutoDetectado();
            case "AutoConfirmado":
                return new AutoConfirmado();
            case "Confirmado":
                return new Confirmado();
            case "BloqueadoEnRevision":
                return new BloqueadoEnRevision();
            case "Cerrado":
                return new Cerrado();
            case "EventoSinRevision":
                return new EventoSinRevision();
            case "PendienteDeCierre":
                return new PendienteDeCierre();
            case "PendienteDeRevision":
                return new PendienteDeRevision();
            case "Rechazado":
                return new Rechazado();
            default:
                throw new RuntimeException("Estado desconocido en la base de datos: " + nombreEstado);
        }
    }

    @Override
    public Estado findById(int id) {
        // Busca el registro por ID de BD para obtener el nombre
        String sql = "SELECT nombre FROM estado WHERE id_estado = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Usa la fábrica para instanciar la clase concreta
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Estado por ID: " + id, e);
        }
        return null;
    }

    /**
     * Método utilitario usado por otros mappers (ej. CambioEstado) para
     * obtener la FK (id_estado) basándose en el nombre del objeto de dominio.
     */
    public int findIdByNombre(String nombre) {
        String sql = "SELECT id_estado FROM estado WHERE nombre = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_estado");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ID de Estado por nombre: " + nombre, e);
        }
        throw new RuntimeException("No se encontró el ID de Estado con nombre: " + nombre);
    }

    @Override
    public List<Estado> findAll() {
        String sql = "SELECT nombre FROM estado";
        List<Estado> estados = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                estados.add(mapResultSet(rs)); // Usa la fábrica
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los Estados.", e);
        }
        return estados;
    }

    @Override
    public Estado findByNombre(String nombre) {
        // La consulta busca el registro por el nombre que es único
        String sql = "SELECT nombre FROM estado WHERE nombre = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 🛑 Usa la fábrica (mapResultSet) para instanciar la clase concreta (ej. new Confirmado())
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Estado por nombre: " + nombre, e);
        }
        return null; // Devuelve null si no se encuentra el estado
    }

    @Override
    public void insert(Estado estado) {
        // Solo se inserta el nombre, dejando que el ID se autogenere.
        // Se usa el nombre del objeto concreto (ej: estado.getNombre() de Confirmado)
        String sql = "INSERT INTO estado (nombre) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.getNombre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar Estado: " + estado.getNombre(), e);
        }
    }

    // El método update no se implementa porque los estados son estáticos,
    // pero si existiera, usaría findIdByNombre para la cláusula WHERE.
}