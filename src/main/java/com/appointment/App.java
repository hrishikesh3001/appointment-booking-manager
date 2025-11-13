package com.appointment;

import javax.swing.SwingUtilities;

import com.appointment.controller.BookingController;
import com.appointment.repository.AppointmentRepository;
import com.appointment.repository.InMemoryAppointmentRepository;
import com.appointment.view.AppointmentSwingView;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	AppointmentRepository repo = new InMemoryAppointmentRepository();
            AppointmentSwingView view = new AppointmentSwingView();
            BookingController controller = new BookingController(repo, view);
            view.setController(controller);
            controller.allAppointments();
            view.setVisible(true);
        });
    }
}
