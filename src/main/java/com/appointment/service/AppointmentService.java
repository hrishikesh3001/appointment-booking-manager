package com.appointment.service;

import java.time.LocalDateTime;
import java.util.List;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.appointment.repository.AppointmentRepository;

public class AppointmentService {

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public Appointment addAppointment(String clientName, LocalDateTime dateTime, String service) {
        Appointment a = new Appointment(null, clientName, dateTime, service, AppointmentStatus.SCHEDULED);
        return repository.save(a);
    }

    public List<Appointment> getAllAppointments() {
        return repository.findAll();
    }

    public void deleteAppointment(String id) {
        repository.deleteById(id);
    }
}