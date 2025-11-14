package com.appointment;

import javax.swing.SwingUtilities;

import com.appointment.controller.BookingController;
import com.appointment.repository.InMemoryAppointmentRepository;
import com.appointment.view.AppointmentSwingView;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppointmentSwingView view = new AppointmentSwingView();
            BookingController controller = new BookingController(new InMemoryAppointmentRepository(), view);
            view.setController(controller);
            view.setVisible(true);
        });
    }
}
