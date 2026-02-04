package com.appointment;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.appointment.controller.BookingController;
import com.appointment.repository.InMemoryAppointmentRepository;
import com.appointment.view.AppointmentSwingView;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppointmentSwingView view = new AppointmentSwingView();
            //In the real app: closing the window should exit the JVM
            view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            
            BookingController controller = new BookingController(new InMemoryAppointmentRepository(), view);
            view.setController(controller);
            view.setVisible(true);
        });
    }
}
