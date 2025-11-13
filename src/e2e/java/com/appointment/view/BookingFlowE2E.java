package com.appointment.view;

import java.time.LocalDateTime;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appointment.controller.BookingController;
import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.appointment.repository.InMemoryAppointmentRepository;

class BookingFlowE2E {

    private FrameFixture window;
    private AppointmentSwingView view;
    private BookingController controller;

    @BeforeEach
    void setUp() {
        // Build GUI safely on EDT
        view = GuiActionRunner.execute(AppointmentSwingView::new);

        // Use in-memory repo for a clean E2E environment
        controller = new BookingController(new InMemoryAppointmentRepository(), view);
        view.setController(controller);

        // Attach AssertJ Swing robot AFTER the controller is set
        window = new FrameFixture(view);
        window.show(); // shows the UI

        // Make sure UI is 100% ready
        window.requireVisible();
        window.robot().waitForIdle();

        // Trigger loading of initial appointments
        GuiActionRunner.execute(() -> controller.allAppointments());
        window.robot().waitForIdle();
    }

    @AfterEach
    void tearDown() {
        if (window != null) {
            try {
                window.close();
            } catch (Exception ignored) {}

            window.cleanUp();
        }
    }

    @Test
    void addAndDeleteAppointment_flowWorks() {

        // ---- Add ----
        window.textBox("clientNameTextBox").setText("Alice");
        window.comboBox("serviceComboBox").selectItem("Haircut");
        window.button("addButton").click();
        window.robot().waitForIdle();

        // Check table updated
        window.table("appointmentTable").requireRowCount(1);
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(1), "Alice");
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(2), "Haircut");

        // ---- Delete ----
        window.table("appointmentTable").selectRows(0);
        window.button("deleteButton").click();
        window.robot().waitForIdle();

        // Table must be empty
        window.table("appointmentTable").requireRowCount(0);
    }

    @Test
    void controllerCallback_updatesView() {
        Appointment a = new Appointment(
            "id-E2E",
            "Bob",
            LocalDateTime.of(2025, 11, 15, 10, 0),
            "Massage",
            AppointmentStatus.SCHEDULED
        );

        // Simulate controller calling back into view
        GuiActionRunner.execute(() -> view.appointmentAdded(a));
        window.robot().waitForIdle();

        window.table("appointmentTable").requireRowCount(1);
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(0), "id-E2E");
    }
}
