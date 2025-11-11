package com.appointment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;

class InMemoryAppointmentRepositoryTest {

    private AppointmentRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryAppointmentRepository();
    }

    @Test
    void testSaveAppointment() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            null, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        Appointment saved = repository.save(appointment);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void testFindById() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            null, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );
        Appointment saved = repository.save(appointment);
        Appointment found = repository.findById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByIdNotFound() {
        Appointment found = repository.findById("999");
        assertThat(found).isNull();
    }

    @Test
    void testFindAll() {
        LocalDateTime date1 = LocalDateTime.of(2025, 11, 15, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 11, 16, 14, 0);

        repository.save(new Appointment(
            null, "John Doe", date1, "Haircut", AppointmentStatus.SCHEDULED
        ));
        repository.save(new Appointment(
            null, "Jane Smith", date2, "Massage", AppointmentStatus.SCHEDULED
        ));

        List<Appointment> all = repository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void testUpdate() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = repository.save(new Appointment(
            null, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        ));

        Appointment updated = new Appointment(
            appointment.getId(),
            "John Doe",
            date,
            "Haircut",
            AppointmentStatus.COMPLETED
        );
        repository.update(updated);

        Appointment found = repository.findById(appointment.getId());
        assertThat(found.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }

    @Test
    void testDelete() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = repository.save(new Appointment(
            null, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        ));

        repository.deleteById(appointment.getId());

        Appointment found = repository.findById(appointment.getId());
        assertThat(found).isNull();
    }

    @Test
    void testSaveWithExistingId() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            "100", "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        Appointment saved = repository.save(appointment);

        assertThat(saved.getId()).isEqualTo("100");
        assertThat(repository.findById("100")).isNotNull();
    }

    @Test
    void testUpdateNonExistentAppointment() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            "999", "John Doe", date, "Haircut", AppointmentStatus.COMPLETED
        );

        // Update non-existent - should do nothing
        repository.update(appointment);

        Appointment found = repository.findById("999");
        assertThat(found).isNull();
    }

    @Test
    void testUpdateWithNullId() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            null, "John Doe", date, "Haircut", AppointmentStatus.COMPLETED
        );

        // Update with null ID - should do nothing
        repository.update(appointment);

        // Verify nothing was added
        assertThat(repository.findAll()).isEmpty();
    }
}
