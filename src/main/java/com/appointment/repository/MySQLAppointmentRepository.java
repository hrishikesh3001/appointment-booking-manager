package com.appointment.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;

public class MySQLAppointmentRepository implements AppointmentRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public MySQLAppointmentRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    @Override
    public Appointment save(Appointment appointment) {
        String sql = "INSERT INTO appointments (customer_name, appointment_date, service_type, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, appointment.getCustomerName());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setString(3, appointment.getServiceType());
            stmt.setString(4, appointment.getStatus().name());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return new Appointment(
                        id,
                        appointment.getCustomerName(),
                        appointment.getAppointmentDate(),
                        appointment.getServiceType(),
                        appointment.getStatus()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
        
        return null;
    }

    @Override
    public Appointment findById(Long id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment by id", e);
        }
        
        return null;
    }

    @Override
    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all appointments", e);
        }
        
        return appointments;
    }

    @Override
    public void update(Appointment appointment) {
        String sql = "UPDATE appointments SET customer_name = ?, appointment_date = ?, service_type = ?, status = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, appointment.getCustomerName());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setString(3, appointment.getServiceType());
            stmt.setString(4, appointment.getStatus().name());
            stmt.setLong(5, appointment.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment", e);
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String customerName = rs.getString("customer_name");
        LocalDateTime appointmentDate = rs.getTimestamp("appointment_date").toLocalDateTime();
        String serviceType = rs.getString("service_type");
        AppointmentStatus status = AppointmentStatus.valueOf(rs.getString("status"));
        
        return new Appointment(id, customerName, appointmentDate, serviceType, status);
    }
}