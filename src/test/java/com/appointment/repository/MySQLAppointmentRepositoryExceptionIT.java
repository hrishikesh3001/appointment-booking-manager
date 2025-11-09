package com.appointment.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;

class MySQLAppointmentRepositoryExceptionIT {

    @Test
    void testSaveWithInvalidConnection() {
        MySQLAppointmentRepository repository = new MySQLAppointmentRepository(
            "jdbc:mysql://invalid:3306/test", "user", "password"
        );

        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            null, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        assertThatThrownBy(() -> repository.save(appointment))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Error saving appointment");
    }

    @Test
    void testFindByIdWithInvalidConnection() {
        MySQLAppointmentRepository repository = new MySQLAppointmentRepository(
            "jdbc:mysql://invalid:3306/test", "user", "password"
        );

        assertThatThrownBy(() -> repository.findById(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Error finding appointment");
    }

    @Test
    void testFindAllWithInvalidConnection() {
        MySQLAppointmentRepository repository = new MySQLAppointmentRepository(
            "jdbc:mysql://invalid:3306/test", "user", "password"
        );

        assertThatThrownBy(() -> repository.findAll())
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Error finding all appointments");
    }

    @Test
    void testUpdateWithInvalidConnection() {
        MySQLAppointmentRepository repository = new MySQLAppointmentRepository(
            "jdbc:mysql://invalid:3306/test", "user", "password"
        );

        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.COMPLETED
        );

        assertThatThrownBy(() -> repository.update(appointment))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Error updating appointment");
    }

    @Test
    void testDeleteWithInvalidConnection() {
        MySQLAppointmentRepository repository = new MySQLAppointmentRepository(
            "jdbc:mysql://invalid:3306/test", "user", "password"
        );

        assertThatThrownBy(() -> repository.deleteById(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Error deleting appointment");
    }
}