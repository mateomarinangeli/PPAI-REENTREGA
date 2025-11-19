package com.redsismica.demo.persistence.Empleado;

import com.redsismica.demo.persistence.Empleado.IIntermediarioBDREmpleado;
import com.redsismica.demo.domain.Empleado;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDREmpleado implements IIntermediarioBDREmpleado {

    private final String dbUrl;

    public IntermediarioBDREmpleado(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private Empleado mapResultSet(ResultSet rs) throws SQLException {
        Empleado emp = new Empleado();
        // 🛑 Mapeo del ID
        emp.setIdEmpleado(rs.getInt("id_empleado"));
        emp.setNombre(rs.getString("nombre"));
        emp.setApellido(rs.getString("apellido"));
        emp.setMail(rs.getString("mail"));
        emp.setTelefono(rs.getString("telefono"));
        return emp;
    }

    @Override
    public Empleado findById(int id) {
        String sql = "SELECT * FROM empleado WHERE id_empleado = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Empleado por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Empleado> findAll() {
        String sql = "SELECT * FROM empleado";
        List<Empleado> empleados = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                empleados.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todos los Empleados.", e);
        }
        return empleados;
    }

    @Override
    public void insert(Empleado empleado) {
        String sql = "INSERT INTO empleado (nombre, apellido, mail, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getMail());
            stmt.setString(4, empleado.getTelefono());
            stmt.executeUpdate();

            // Recuperar el ID y asignarlo al objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empleado.setIdEmpleado(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar Empleado.", e);
        }
    }

    @Override
    public void update(Empleado empleado) {
        String sql = "UPDATE empleado SET nombre = ?, apellido = ?, mail = ?, telefono = ? WHERE id_empleado = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getMail());
            stmt.setString(4, empleado.getTelefono());
            stmt.setInt(5, empleado.getIdEmpleado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar Empleado ID: " + empleado.getIdEmpleado(), e);
        }
    }
}