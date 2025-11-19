package com.redsismica.demo.persistence.Sesion;

import com.redsismica.demo.domain.Sesion;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import com.redsismica.demo.persistence.Usuario.IIntermediarioBDRUsuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRSesion implements IIntermediarioBDRSesion {

    private final String dbUrl;
    private final IIntermediarioBDRUsuario usuarioMapper;

    public IntermediarioBDRSesion(@Value("${spring.datasource.url}") String dbUrl,
                                      IIntermediarioBDRUsuario usuarioMapper) {
        this.dbUrl = dbUrl;
        this.usuarioMapper = usuarioMapper;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private Sesion mapResultSet(ResultSet rs) throws SQLException {
        Sesion sesion = new Sesion();
        sesion.setIdSesion(rs.getInt("id_sesion"));

        // Definimos el formateador correcto para "yyyy-MM-dd HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String fechaInicioTexto = rs.getString("fecha_hora_inicio");
        String fechaFinTexto = rs.getString("fecha_hora_fin");

        if (fechaInicioTexto != null) {
            sesion.setFechaHoraInicio(LocalDateTime.parse(fechaInicioTexto, formatter));
        }

        if (fechaFinTexto != null) {
            sesion.setFechaHoraFin(LocalDateTime.parse(fechaFinTexto, formatter));
        }

        // Ensamblaje de Usuario
        int usuarioId = rs.getInt("id_usuario");
        if (!rs.wasNull()) {
            sesion.setUsuario(usuarioMapper.findById(usuarioId));
        }

        return sesion;
    }


    @Override
    public Sesion findById(int id) {
        String sql = "SELECT * FROM sesion WHERE id_sesion = ?";
        // ... (Implementación de findById) ...
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Sesion por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Sesion> findAllByUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM sesion WHERE id_usuario = ? ORDER BY fecha_hora_inicio DESC";
        List<Sesion> sesiones = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) sesiones.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Sesiones por usuario ID: " + usuarioId, e);
        }
        return sesiones;
    }

    @Override
    public void insert(Sesion sesion) {
        String sql = "INSERT INTO sesion (fecha_hora_inicio, fecha_hora_fin, id_usuario) VALUES (?, ?, ?)";

        String fechaInicioTexto = sesion.getFechaHoraInicio() != null ? sesion.getFechaHoraInicio().toString() : null;
        String fechaFinTexto = sesion.getFechaHoraFin() != null ? sesion.getFechaHoraFin().toString() : null;
        int usuarioId = sesion.getUsuario().getIdUsuario();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, fechaInicioTexto);
            stmt.setString(2, fechaFinTexto);
            stmt.setInt(3, usuarioId);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) sesion.setIdSesion(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar Sesion.", e);
        }
    }

    @Override
    public List<Sesion> findAll() {
        String sql = "SELECT * FROM sesion ORDER BY fecha_hora_inicio DESC";
        List<Sesion> sesiones = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sesiones.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todas las Sesiones.", e);
        }
        return sesiones;
    }

    @Override
    public void update(Sesion sesion) {
        String sql = "UPDATE sesion SET fecha_hora_inicio = ?, fecha_hora_fin = ?, id_usuario = ? WHERE id_sesion = ?";

        String fechaInicioTexto = sesion.getFechaHoraInicio() != null ? sesion.getFechaHoraInicio().toString() : null;
        String fechaFinTexto = sesion.getFechaHoraFin() != null ? sesion.getFechaHoraFin().toString() : null;
        int usuarioId = sesion.getUsuario().getIdUsuario();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fechaInicioTexto);
            stmt.setString(2, fechaFinTexto);
            stmt.setInt(3, usuarioId);

            // Usamos el ID de la sesión para la cláusula WHERE
            stmt.setInt(4, sesion.getIdSesion());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar Sesion ID: " + sesion.getIdSesion(), e);
        }
    }
}