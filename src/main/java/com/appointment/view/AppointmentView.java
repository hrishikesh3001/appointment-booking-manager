package com.appointment.view;

import java.util.List;
import com.appointment.model.Appointment;

public interface AppointmentView {
    void showAllAppointments(List<Appointment> appointments);
    void appointmentAdded(Appointment appointment);
    void appointmentRemoved(String appointmentId);
    void showError(String message);
}
