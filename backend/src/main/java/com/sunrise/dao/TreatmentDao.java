package com.sunrise.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sunrise.model.Treatment;
import com.sunrise.util.DbConnection;

public class TreatmentDao {
    public List<Treatment> findAll() {
        List<Treatment> list = new ArrayList<>();
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM treatments ORDER BY name");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Could not list treatments", e);
        }
        return list;
    }

    public Optional<Treatment> findById(int id) {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM treatments WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not find treatment", e);
        }
        return Optional.empty();
    }

    private Treatment map(ResultSet rs) throws SQLException {
        Treatment t = new Treatment();
        t.setId(rs.getInt("id"));
        t.setName(rs.getString("name"));
        t.setTreatmentFee(rs.getBigDecimal("treatment_fee"));
        t.setConsultationFee(rs.getBigDecimal("consultation_fee"));
        return t;
    }
}
