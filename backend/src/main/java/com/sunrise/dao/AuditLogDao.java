package com.sunrise.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sunrise.model.AuditLog;
import com.sunrise.util.DbConnection;

public class AuditLogDao {
    public void insert(AuditLog log) {
        String sql = "INSERT INTO audit_logs (user_id, username, action, details) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (log.getUserId() == null) {
                stmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(1, log.getUserId());
            }
            stmt.setString(2, log.getUsername());
            stmt.setString(3, log.getAction());
            stmt.setString(4, log.getDetails());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Could not write audit log", e);
        }
    }

    public List<AuditLog> findRecent(int limit) {
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT ?")) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog();
                    log.setId(rs.getInt("id"));
                    int userId = rs.getInt("user_id");
                    log.setUserId(rs.wasNull() ? null : userId);
                    log.setUsername(rs.getString("username"));
                    log.setAction(rs.getString("action"));
                    log.setDetails(rs.getString("details"));
                    log.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not list audit logs", e);
        }
        return logs;
    }
}
