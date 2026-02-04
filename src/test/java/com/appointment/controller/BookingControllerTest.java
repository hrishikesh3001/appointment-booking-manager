package com.appointment.controller;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.appointment.repository.AppointmentRepository;
import com.appointment.view.AppointmentView;

class BookingControllerTest {

    private AppointmentRepository repository;
    private AppointmentView view;
    private BookingController controller;

    @BeforeEach
    void setup() {
        repository = mock(AppointmentRepository.class);
        view = mock(AppointmentView.class);
        controller = new BookingController(repository, view);
    }

    @Test
    void allAppointments_loadsFromRepository_andShowsInView() {
        Appointment a1 = new Appointment("1", "John",
                LocalDateTime.now(), "Cut", AppointmentStatus.SCHEDULED);
        Appointment a2 = new Appointment("2", "Jane",
                LocalDateTime.now(), "Color", AppointmentStatus.SCHEDULED);

        when(repository.findAll()).thenReturn(List.of(a1, a2));

        controller.allAppointments();

        verify(view).showAllAppointments(List.of(a1, a2));
    }

    @Test
    void newAppointment_validData_savesAndNotifiesView() {
        LocalDateTime dt = LocalDateTime.of(2025, 11, 10, 10, 0);

        // This is the Appointment the controller is expected to create and pass to the repo
        Appointment toSave = new Appointment(
                null, "Alex", dt, "Massage", AppointmentStatus.SCHEDULED);

        Appointment saved = new Appointment(
                "123", "Alex", dt, "Massage", AppointmentStatus.SCHEDULED);

        when(repository.save((toSave))).thenReturn(saved);

        controller.newAppointment("Alex", dt, "Massage");

        verify(repository).save((toSave));
        verify(view).appointmentAdded(saved);
    }

    @Test
    void newAppointment_invalidCustomerName_showsError_andDoesNotCallRepository() {
        LocalDateTime dt = LocalDateTime.of(2025, 11, 10, 10, 0);

        controller.newAppointment(" ", dt, "Massage");

        verify(view).showError("Customer name cannot be null or empty");
        verifyNoInteractions(repository);
    }

    @Test
    void deleteAppointment_deletesAndNotifiesView() {
        controller.deleteAppointment("99");

        verify(repository).deleteById("99");
        verify(view).appointmentRemoved("99");
    }
}
