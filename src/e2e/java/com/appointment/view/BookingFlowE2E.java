package com.appointment.view;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appointment.controller.BookingController;
import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.appointment.repository.InMemoryAppointmentRepository;

import java.time.LocalDateTime;

@GUITest
class BookingFlowE2E {

    private FrameFixture window;
    private AppointmentSwingView view;

    @BeforeEach
    void setUp() {
        view = GuiActionRunner.execute(() -> {
            AppointmentSwingView v = new AppointmentSwingView();
            v.setController(new BookingController(new InMemoryAppointmentRepository(), v));
            return v;
        });
        window = new FrameFixture(view);
        window.show();
    }

    @AfterEach
    void tearDown() {
        window.cleanUp();
        view.dispose();
    }

    @Test
    void addAndDeleteAppointment_flowWorks() {
        window.textBox("clientNameTextBox").enterText("Alice");
        window.comboBox("serviceComboBox").selectItem("Haircut");

        window.spinner("dateSpinner")
              .enterTextAndCommit("2025-11-15 10:00");

        window.button("addButton").click();
        window.table("appointmentTable").requireRowCount(1);

        window.table("appointmentTable").selectRows(0);
        window.button("deleteButton").click();
        window.table("appointmentTable").requireRowCount(0);
    }

    @Test
    void controllerCallback_updatesView() {
        Appointment a = new Appointment(
                "id-E2E",
                "Bob",
                LocalDateTime.of(2025,11,15,10,0),
                "Massage",
                AppointmentStatus.SCHEDULED
        );

        GuiActionRunner.execute(() -> view.appointmentAdded(a));

        window.table("appointmentTable").requireRowCount(1);
        window.table("appointmentTable").requireCellValue(
                org.assertj.swing.data.TableCell.row(0).column(0),
                "id-E2E"
        );
    }
}
