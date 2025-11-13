package com.appointment.e2e;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.appointment.controller.BookingController;
import com.appointment.repository.MongoAppointmentRepository;
import com.appointment.view.AppointmentSwingView;

@Testcontainers
class AppointmentAppE2E {

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    private FrameFixture window;
    private AppointmentSwingView view;
    private BookingController controller;
    private MongoAppointmentRepository repository;

    @BeforeEach
    void setUp() {
        // Start Mongo via Testcontainers (Docker) and build real repository
        String uri = mongo.getConnectionString();
        repository = new MongoAppointmentRepository(uri);

        // Build real Swing UI + controller (full stack) on EDT
        view = GuiActionRunner.execute(AppointmentSwingView::new);
        controller = new BookingController(repository, view);
        view.setController(controller);

        // Attach AssertJ robot
        window = new FrameFixture(view);
        window.show();

        // Make sure the UI is ready for interaction
        window.requireVisible();
        window.focus();
        window.robot().settings().delayBetweenEvents(30);
        window.robot().waitForIdle();

        // load existing appointments (if any)
        GuiActionRunner.execute(() -> controller.allAppointments());
        window.robot().waitForIdle();
    }

    @AfterEach
    void tearDown() {
        if (window != null) {
            try { window.close(); } catch (Exception ignored) {}
            window.cleanUp();
        }
    }

    @Test
    @Timeout(60) // hard stop: prevents Maven hanging forever
    void userCanAddAppointment_endToEndWithMongo() {
        // This is a "real user" flow: type name, select service, click "Add"
        window.textBox("clientNameTextBox").requireEnabled().setText("Eve");
        window.comboBox("serviceComboBox").requireEnabled().selectItem("Color");
        window.button("addButton").requireEnabled().click();
        window.robot().waitForIdle();

        // Verify the UI state (and indirectly that the full stack worked)
        window.table("appointmentTable").requireRowCount(1);
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(1), "Eve");
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(2), "Color");
    }
}
