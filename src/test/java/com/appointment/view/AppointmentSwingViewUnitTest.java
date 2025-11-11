// src/test/java/com/appointment/view/AppointmentSwingViewUnitTest.java
package com.appointment.view;

import static org.mockito.Mockito.*;

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

class AppointmentSwingViewUnitTest {

    private FrameFixture window;
    private AppointmentSwingView view;
    private BookingController controller;

    @BeforeEach
    void setUp() {
        // Create Swing UI on EDT
        view = GuiActionRunner.execute(AppointmentSwingView::new);
        // Mock controller and wire it
        controller = mock(BookingController.class);
        view.setController(controller);
        // Build the FrameFixture and show the window
        window = new FrameFixture(view);
        window.show(); // important: shows the frame so fixtures find components
    }

    @AfterEach
    void tearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }

    @Test
    void clickingAddDelegatesToController() {
        window.textBox("clientNameTextBox").enterText("Alice");
        window.comboBox("serviceComboBox").selectItem("Haircut");
        window.button("addButton").click();

        verify(controller).newAppointment(eq("Alice"), any(LocalDateTime.class), eq("Haircut"));
    }

    @Test
    void appointmentAddedAddsRowToTable() {
        Appointment a = new Appointment(
            "id-1", "Bob",
            LocalDateTime.of(2025, 11, 15, 10, 0),
            "Massage", AppointmentStatus.SCHEDULED
        );

        GuiActionRunner.execute(() -> view.appointmentAdded(a));

        window.table("appointmentTable").requireRowCount(1);
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(0), "id-1");
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(1), "Bob");
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(2), "Massage");
    }
}
