package com.appointment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.appointment.repository.AppointmentRepository;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* ---------- red ---------- */
    @Test
    void testAddAppointment() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 20, 10, 0);
        Appointment toSave = new Appointment(null, "Bob", date, "Trim", AppointmentStatus.SCHEDULED);
        Appointment saved = new Appointment("id123", "Bob", date, "Trim", AppointmentStatus.SCHEDULED);

        when(repository.save(toSave)).thenReturn(saved);

        Appointment result = service.addAppointment("Bob", date, "Trim");

        assertThat(result).isEqualTo(saved);
        verify(repository).save(toSave);
    }

    @Test
    void testFindAll() {
        Appointment a1 = new Appointment("id1", "A", LocalDateTime.now(), "Cut", AppointmentStatus.SCHEDULED);
        Appointment a2 = new Appointment("id2", "B", LocalDateTime.now(), "Color", AppointmentStatus.SCHEDULED);
        when(repository.findAll()).thenReturn(List.of(a1, a2));

        List<Appointment> all = service.getAllAppointments();

        assertThat(all).containsExactly(a1, a2);
    }

    @Test
    void testDeleteAppointment() {
        service.deleteAppointment("id99");
        verify(repository).deleteById("id99");
    }
}