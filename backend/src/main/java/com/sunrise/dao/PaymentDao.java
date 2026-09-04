package com.sunrise.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sunrise.model.Payment;
import com.sunrise.util.DbConnection;

public class PaymentDao {
    public int insert(Payment payment) {
        String sql = "INSERT INTO payments (appointment_id, amount, payment_method) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getAppointmentId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not record payment", e);
        }
        return -1;
    }

    public List<Payment> findByAppointment(int appointmentId) {
        List<Payment> list = new ArrayList<>();
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM payments WHERE appointment_id = ? ORDER BY payment_date")) {
            stmt.setInt(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not load payments", e);
        }
        return list;
    }

    public BigDecimal sumAll() {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(SUM(amount), 0) FROM payments");
                ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DaoException("Could not sum payments", e);
        }
    }

    public void deleteByAppointment(int appointmentId) {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM payments WHERE appointment_id = ?")) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Could not delete payments", e);
        }
    }

    private Payment map(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getInt("id"));
        p.setAppointmentId(rs.getInt("appointment_id"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
        p.setPaymentMethod(rs.getString("payment_method"));
        return p;
    }
}
