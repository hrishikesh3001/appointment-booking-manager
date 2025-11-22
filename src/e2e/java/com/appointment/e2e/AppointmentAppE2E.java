package com.appointment.e2e;

import org.assertj.swing.annotation.GUITest;
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
@GUITest
class AppointmentAppE2E {

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    private FrameFixture window;
    private AppointmentSwingView view;
    private BookingController controller;
    private MongoAppointmentRepository repository;

    @BeforeEach
    void setUp() {
        String uri = mongo.getConnectionString();
        repository = new MongoAppointmentRepository(uri);

        view = GuiActionRunner.execute(AppointmentSwingView::new);
        controller = new BookingController(repository, view);
        view.setController(controller);

        window = new FrameFixture(view);
        window.show();

        // Initial load
        GuiActionRunner.execute(() -> controller.allAppointments());
        window.robot().waitForIdle();

        window.focus();
        window.robot().settings().delayBetweenEvents(30);
    }

    @AfterEach
    void tearDown() {
        if (window != null) {
            window.cleanUp();	//releases the robot and hides window
        }
        if (view != null) {
        	GuiActionRunner.execute(() -> view.dispose());
        }
    }

    @Test
    @Timeout(60)
    void userCanAddAppointment_endToEndWithMongo() {
        window.textBox("clientNameTextBox").setText("Eve");
        window.comboBox("serviceComboBox").selectItem("Color");

        window.spinner("dateSpinner")
              .enterTextAndCommit("2025-11-20 09:30");

        window.button("addButton").click();
        window.robot().waitForIdle();

        window.table("appointmentTable").requireRowCount(1);
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(1), "Eve");
        window.table("appointmentTable").requireCellValue(TableCell.row(0).column(2), "Color");
    }
}
