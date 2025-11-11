package com.appointment.view;

import static org.assertj.swing.edt.GuiActionRunner.showInEdt;
import static org.assertj.swing.fixture.Containers.showInEdt;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.*;
import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import java.time.LocalDateTime;

class AppointmentSwingViewTest {

    private FrameFixture window;
    private AppointmentSwingView view;

    @BeforeEach
    void setUp() {
        view = new AppointmentSwingView();
        window = showInEdt(view);
    }

    @AfterEach
    void tearDown() {
        window.cleanUp();
    }

    /* ---------- red ---------- */
    @Test
    void testAddAppointmentShowsInTable() {
        // enter data
        window.textBox("clientNameTextBox").enterText("Alice");
        window.comboBox("serviceComboBox").selectItem("Haircut");
        window.button("addButton").click();

        // verify table
        window.table("appointmentTable").requireRowCount(1);
        window.table("appointmentTable").requireCellValue(0, 0, "Alice");
        window.table("appointmentTable").requireCellValue(0, 1, "Haircut");
    }
}