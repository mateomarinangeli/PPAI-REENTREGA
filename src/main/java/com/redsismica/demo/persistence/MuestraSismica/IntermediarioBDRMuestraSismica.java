package com.redsismica.demo.persistence.MuestraSismica;

import com.redsismica.demo.domain.MuestraSismica;
import com.redsismica.demo.domain.DetalleMuestraSismica;
import com.redsismica.demo.persistence.DetalleMuestraSismica.IIntermediarioBDRDetalleMuestraSismica;
import com.redsismica.demo.persistence.SerieTemporal.IIntermediarioBDRSerieTemporal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IntermediarioBDRMuestraSismica implements IIntermediarioBDRMuestraSismica {

    private final String dbUrl;
    private final IIntermediarioBDRSerieTemporal serieTemporalMapper;
    @Lazy
    private IIntermediarioBDRDetalleMuestraSismica detalleMapper;

    public IntermediarioBDRMuestraSismica(
            @Value("${spring.datasource.url}") String dbUrl,
            IIntermediarioBDRSerieTemporal serieTemporalMapper) {
        this.dbUrl = dbUrl;
        this.serieTemporalMapper = serieTemporalMapper;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    @Autowired
    public void setDetalleMapper(@Lazy IIntermediarioBDRDetalleMuestraSismica detalleMapper) {
        this.detalleMapper = detalleMapper;
    }

    private MuestraSismica mapResultSet(ResultSet rs) throws SQLException {
        MuestraSismica muestra = new MuestraSismica();

        int idMuestra = rs.getInt("id_muestra");
        muestra.setIdMuestraSismica(idMuestra); // IMPORTANTE: si no hacés esto nunca vas a buscar detalles

        muestra.setFechaHoraMuestra(
                rs.getTimestamp("fecha_hora_muestra").toLocalDateTime()
        );

        // Cargar detalles usando el mapper LAZY
        if (detalleMapper != null) {
            List<DetalleMuestraSismica> detalles =
                    detalleMapper.findAllByMuestraId(idMuestra);
            muestra.setDetalleMuestraSismica(detalles);
        }

        return muestra;
    }


    @Override
    public MuestraSismica findById(int id) {
        String sql = "SELECT * FROM muestra_sismica WHERE id_muestra = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar MuestraSismica por ID.", e);
        }
        return null;
    }

    @Override
    public List<MuestraSismica> findAll() {
        String sql = "SELECT * FROM muestra_sismica";
        List<MuestraSismica> muestras = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                muestras.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todas las MuestraSismica.", e);
        }
        return muestras;
    }

    @Override
    public List<MuestraSismica> findAllBySerieId(int idSerie) {
        String sql = "SELECT * FROM muestra_sismica WHERE id_serie = ? ORDER BY fecha_hora_muestra ASC";
        List<MuestraSismica> muestras = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSerie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    muestras.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Muestras para Serie ID " + idSerie, e);
        }
        return muestras;
    }

    @Override
    public void insert(MuestraSismica muestra, int idSerie) {
        String sql = "INSERT INTO muestra_sismica (fecha_hora_muestra, id_serie) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(muestra.getFechaHoraMuestra()));
            stmt.setInt(2, idSerie);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Asumimos que tienes un setIdMuestra o similar
                    // muestra.setIdMuestra(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar MuestraSismica para Serie ID: " + idSerie, e);
        }
    }

    @Override
    public void insertDetalle(DetalleMuestraSismica detalle, int idMuestra) {
        String sql = "INSERT INTO detalle_muestra_sismica (id_muestra, valor, id_tipo_de_dato) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idMuestra);
            stmt.setDouble(2, detalle.getValor());
            stmt.setInt(3, detalle.getTipoDeDato().getIdTipoDeDato()); // necesitas un idTipoDeDato

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar DetalleMuestraSismica para Muestra ID: " + idMuestra, e);
        }
    }
}
