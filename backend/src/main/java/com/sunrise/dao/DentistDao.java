package com.sunrise.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sunrise.model.Dentist;
import com.sunrise.util.DbConnection;

public class DentistDao {
    public List<Dentist> findAll() {
        List<Dentist> list = new ArrayList<>();
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM dentists ORDER BY full_name");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Could not list dentists", e);
        }
        return list;
    }

    public Optional<Dentist> findById(int id) {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM dentists WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not find dentist", e);
        }
        return Optional.empty();
    }

    private Dentist map(ResultSet rs) throws SQLException {
        Dentist d = new Dentist();
        d.setId(rs.getInt("id"));
        d.setFullName(rs.getString("full_name"));
        d.setSpecialization(rs.getString("specialization"));
        return d;
    }
}
