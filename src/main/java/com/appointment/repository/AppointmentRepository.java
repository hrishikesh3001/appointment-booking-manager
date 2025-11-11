package com.appointment.repository;

import java.util.List;
import com.appointment.model.Appointment;

public interface AppointmentRepository {
	Appointment save(Appointment appointment);
	Appointment findById(String id);
	List<Appointment> findAll();
	void update(Appointment appointment);
	void deleteById(String id);
}
