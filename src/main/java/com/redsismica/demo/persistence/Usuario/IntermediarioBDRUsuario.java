package com.redsismica.demo.persistence.Usuario;

import com.redsismica.demo.domain.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import com.redsismica.demo.persistence.Empleado.IIntermediarioBDREmpleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRUsuario implements IIntermediarioBDRUsuario {

    private final String dbUrl;
    private final IIntermediarioBDREmpleado empleadoMapper;

    public IntermediarioBDRUsuario(@Value("${spring.datasource.url}") String dbUrl,
                                       IIntermediarioBDREmpleado empleadoMapper) {
        this.dbUrl = dbUrl;
        this.empleadoMapper = empleadoMapper;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private Usuario mapResultSet(ResultSet rs) throws SQLException {
        Usuario user = new Usuario();
        user.setIdUsuario(rs.getInt("id_usuario"));
        user.setNombreUsuario(rs.getString("nombre_usuario"));
        user.setContraseña(rs.getString("contrasena")); // Cuidado: Nunca mapear esto en un sistema real

        // Ensamblaje de la referencia Empleado
        int empleadoId = rs.getInt("id_empleado");
        if (!rs.wasNull()) {
            user.setEmpleado(empleadoMapper.findById(empleadoId));
        }

        return user;
    }

    @Override
    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Usuario por ID: " + id, e);
        }
        return null;
    }

    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuario WHERE nombre_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Usuario por nombre: " + nombreUsuario, e);
        }
        return null;
    }

    @Override
    public void insert(Usuario user) {
        String sql = "INSERT INTO usuario (nombre_usuario, contrasena, id_empleado) VALUES (?, ?, ?)";
        int empleadoId = user.getEmpleado().getIdEmpleado();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getNombreUsuario());
            stmt.setString(2, user.getContraseña());
            stmt.setInt(3, empleadoId);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) user.setIdUsuario(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar Usuario.", e);
        }
    }

    // Omitiendo findAll() y update() por brevedad, su lógica sigue el patrón.
    @Override
    public List<Usuario> findAll() { /* ... implementación ... */ return null; }
    @Override
    public void update(Usuario user) { /* ... implementación ... */ }
}