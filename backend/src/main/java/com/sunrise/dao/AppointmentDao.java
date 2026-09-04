package com.sunrise.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sunrise.model.Appointment;
import com.sunrise.model.AppointmentStatus;
import com.sunrise.util.DbConnection;

public class AppointmentDao {

    private static final String SELECT_JOIN = """
            SELECT a.*, d.full_name AS dentist_name, t.name AS treatment_name
            FROM appointments a
            JOIN dentists d ON a.dentist_id = d.id
            JOIN treatments t ON a.treatment_id = t.id
            """;

    public boolean isSlotFree(int dentistId, LocalDate date, LocalTime time, int excludeId) {
        String sql = """
                SELECT COUNT(*) FROM appointments
                WHERE dentist_id = ? AND appointment_date = ? AND appointment_time = ?
                  AND status = 'SCHEDULED' AND id <> ?
                """;
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dentistId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(time));
            stmt.setInt(4, excludeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Could not check dentist slot", e);
        }
    }

    public int nextSequence() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM appointments";
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException("Could not allocate appointment number", e);
        }
        return 1;
    }

    public int insert(Appointment a) {
        String sql = """
                INSERT INTO appointments
                (appointment_no, patient_name, address, contact_number, dentist_id, treatment_id,
                 appointment_date, appointment_time, total_amount, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(stmt, a);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not create appointment", e);
        }
        return -1;
    }

    public boolean update(Appointment a) {
        String sql = """
                UPDATE appointments SET
                    appointment_no=?, patient_name=?, address=?, contact_number=?, dentist_id=?,
                    treatment_id=?, appointment_date=?, appointment_time=?, total_amount=?, status=?
                WHERE id=?
                """;
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            bind(stmt, a);
            stmt.setInt(11, a.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Could not update appointment", e);
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM appointments WHERE id = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Could not delete appointment", e);
        }
    }

    public Optional<Appointment> findById(int id) {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(SELECT_JOIN + " WHERE a.id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not load appointment", e);
        }
        return Optional.empty();
    }

    public Optional<Appointment> findByNumber(String number) {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(SELECT_JOIN + " WHERE a.appointment_no = ?")) {
            stmt.setString(1, number);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not search appointment", e);
        }
        return Optional.empty();
    }

    public List<Appointment> findAll() {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(SELECT_JOIN + " ORDER BY a.appointment_date DESC, a.appointment_time DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Could not list appointments", e);
        }
        return list;
    }

    public long countAll() {
        return scalar("SELECT COUNT(*) FROM appointments");
    }

    public long countByStatus(AppointmentStatus status) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE status = ?";
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Could not count appointments", e);
        }
    }

    public long countToday() {
        return scalar("SELECT COUNT(*) FROM appointments WHERE appointment_date = CURDATE() AND status = 'SCHEDULED'");
    }

    public java.math.BigDecimal sumAmountExcludingCancelled() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM appointments WHERE status <> 'CANCELLED'";
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DaoException("Could not sum amounts", e);
        }
    }

    private long scalar(String sql) {
        try (Connection conn = DbConnection.getInstance().open();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new DaoException("Query failed", e);
        }
    }

    private void bind(PreparedStatement stmt, Appointment a) throws SQLException {
        stmt.setString(1, a.getAppointmentNo());
        stmt.setString(2, a.getPatientName());
        stmt.setString(3, a.getAddress());
        stmt.setString(4, a.getContactNumber());
        stmt.setInt(5, a.getDentistId());
        stmt.setInt(6, a.getTreatmentId());
        stmt.setDate(7, Date.valueOf(a.getAppointmentDate()));
        stmt.setTime(8, Time.valueOf(a.getAppointmentTime()));
        stmt.setBigDecimal(9, a.getTotalAmount());
        stmt.setString(10, a.getStatus().name());
    }

    private Appointment map(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setId(rs.getInt("id"));
        a.setAppointmentNo(rs.getString("appointment_no"));
        a.setPatientName(rs.getString("patient_name"));
        a.setAddress(rs.getString("address"));
        a.setContactNumber(rs.getString("contact_number"));
        a.setDentistId(rs.getInt("dentist_id"));
        a.setTreatmentId(rs.getInt("treatment_id"));
        a.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        a.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
        a.setTotalAmount(rs.getBigDecimal("total_amount"));
        a.setStatus(AppointmentStatus.valueOf(rs.getString("status")));
        a.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        a.setDentistName(rs.getString("dentist_name"));
        a.setTreatmentName(rs.getString("treatment_name"));
        return a;
    }
}
