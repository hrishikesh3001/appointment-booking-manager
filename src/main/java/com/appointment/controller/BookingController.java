package com.appointment.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.appointment.repository.AppointmentRepository;
import com.appointment.view.AppointmentView;

public class BookingController {

    private final AppointmentRepository repository;
    private final AppointmentView view;

    public BookingController(AppointmentRepository repository, AppointmentView view) {
        this.repository = repository;
        this.view = view;
    }

    public void allAppointments() {
        List<Appointment> all = repository.findAll();
        view.showAllAppointments(all);
    }

    public void newAppointment(String customerName, LocalDateTime when, String serviceType) {
        try {
            Appointment toSave = new Appointment(null, customerName, when, serviceType, AppointmentStatus.SCHEDULED);
            Appointment saved = repository.save(toSave);
            view.appointmentAdded(saved);
        } catch (IllegalArgumentException ex) {
            view.showError(ex.getMessage());
        }
    }

    public void deleteAppointment(String id) {
        repository.deleteById(id);
        view.appointmentRemoved(id);
    }
}
