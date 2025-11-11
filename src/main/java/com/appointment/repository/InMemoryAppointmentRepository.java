package com.appointment.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.appointment.model.Appointment;

public class InMemoryAppointmentRepository implements AppointmentRepository {

    private final Map<String, Appointment> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Appointment save(Appointment appointment) {
        String id = appointment.getId() != null ? 
                  appointment.getId() : idGenerator.getAndIncrement();
        
        Appointment toSave = new Appointment(
            id,
            appointment.getCustomerName(),
            appointment.getAppointmentDate(),
            appointment.getServiceType(),
            appointment.getStatus()
        );
        
        database.put(id, toSave);
        return toSave;
    }

    @Override
    public Appointment findById(String id) {
        return database.get(id);
    }

    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void update(Appointment appointment) {
        if (appointment.getId() != null && database.containsKey(appointment.getId())) {
            database.put(appointment.getId(), appointment);
        }
    }

    @Override
    public void deleteById(String id) {
        database.remove(id);
    }
}