package com.appointment.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appointment.controller.BookingController;
import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;


class AppointmentSwingViewUnitTest {

    private AppointmentSwingView view;
    private BookingController controllerMock;

    @BeforeEach
    void setUp() {
        view = new AppointmentSwingView();
        controllerMock = mock(BookingController.class);
        view.setController(controllerMock);
    }

    // ---------- helpers to access private Swing fields ----------

    private JTextField clientNameField() {
        return (JTextField) getField("clientNameTextBox");
    }

    @SuppressWarnings("unchecked")
    private JComboBox<String> serviceCombo() {
        return (JComboBox<String>) getField("serviceComboBox");
    }

    private JButton addButton() {
        return (JButton) getField("addButton");
    }

    private JButton deleteButton() {
        return (JButton) getField("deleteButton");
    }

    private JTable appointmentTable() {
        return (JTable) getField("appointmentTable");
    }

    private Object getField(String name) {
        try {
            Field f = AppointmentSwingView.class.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- tests for "Add" button listener (lambda$0) ----------

    @Test
    void clickingAddWithValidData_callsControllerNewAppointment() {
        clientNameField().setText("Alice");
        serviceCombo().setSelectedItem("Haircut");

        addButton().doClick();   // triggers add button listener

        verify(controllerMock).newAppointment(
                eq("Alice"),
                any(LocalDateTime.class),
                eq("Haircut"));
    }

    @Test
    void clickingAddWithEmptyName_showsErrorAndDoesNotCallController() {
        // empty name -> name.isBlank() branch
        clientNameField().setText("   ");
        serviceCombo().setSelectedItem("Massage");

        addButton().doClick();

        // no call to controller
        verify(controllerMock, never())
                .newAppointment(anyString(), any(LocalDateTime.class), anyString());
    }

    @Test
    void clickingAddWithNullController_doesNothing() {
        // cover controller == null branch
        AppointmentSwingView viewNoController = new AppointmentSwingView();
        JTextField nameField = getFieldFromOther(viewNoController, "clientNameTextBox", JTextField.class);
        JButton addBtn = getFieldFromOther(viewNoController, "addButton", JButton.class);

        nameField.setText("Bob");

        // should not throw and should not call any controller (because there is none)
        addBtn.doClick(); // if NPE happens, the test will fail
    }

    // ---------- tests for "Delete" button listener (lambda$1) ----------

    @Test
    void clickingDeleteWithSelection_callsControllerDelete() {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[] { "id-1", "Alice", "Cut", LocalDateTime.now(), AppointmentStatus.SCHEDULED });

        JTable table = appointmentTable();
        table.setRowSelectionInterval(0, 0);

        deleteButton().doClick();

        verify(controllerMock).deleteAppointment("id-1");
    }

    @Test
    void clickingDeleteWithNoSelection_doesNothing() {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[] { "id-1", "Alice", "Cut", LocalDateTime.now(), AppointmentStatus.SCHEDULED });

        // no selection
        deleteButton().doClick();

        verify(controllerMock, never()).deleteAppointment(anyString());
    }

    @Test
    void clickingDeleteWithNullController_doesNothing() {
        // another instance without controller -> controller == null branch
        AppointmentSwingView viewNoController = new AppointmentSwingView();
        DefaultTableModel model = viewNoController.getTableModel();
        model.addRow(new Object[] { "id-1", "Alice", "Cut", LocalDateTime.now(), AppointmentStatus.SCHEDULED });

        JTable table = getFieldFromOther(viewNoController, "appointmentTable", JTable.class);
        JButton deleteBtn = getFieldFromOther(viewNoController, "deleteButton", JButton.class);

        table.setRowSelectionInterval(0, 0);

        deleteBtn.doClick(); // should not throw, and no controller to verify
    }

    // ---------- tests for appointmentRemoved(String) branches ----------

    @Test
    void appointmentRemovedRemovesExistingRow() {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[] { "id-1", "Alice", "Cut", LocalDateTime.now(), AppointmentStatus.SCHEDULED });
        model.addRow(new Object[] { "id-2", "Bob", "Color", LocalDateTime.now(), AppointmentStatus.SCHEDULED });

        view.appointmentRemoved("id-1");

        assertThat(model.getRowCount()).isEqualTo(1);
        assertThat(model.getValueAt(0, 0)).isEqualTo("id-2");
    }

    @Test
    void appointmentRemovedWithUnknownIdDoesNotChangeTable() {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[] { "id-1", "Alice", "Cut", LocalDateTime.now(), AppointmentStatus.SCHEDULED });

        view.appointmentRemoved("unknown-id");

        assertThat(model.getRowCount()).isEqualTo(1);
        assertThat(model.getValueAt(0, 0)).isEqualTo("id-1");
    }

    // ---------- small helper for accessing fields of a different instance ----------

    @SuppressWarnings("unchecked")
    private <T> T getFieldFromOther(AppointmentSwingView other, String name, Class<T> type) {
        try {
            Field f = AppointmentSwingView.class.getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(other);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void showAllAppointments_withEmptyList_clearsTable() {
        DefaultTableModel model = (DefaultTableModel) view.getTableModel();

        // Arrange: put one row in the table so we can see it being cleared
        Appointment existing = new Appointment(
            "1", "Alice",
            LocalDateTime.of(2025, 11, 10, 9, 0),
            "Cut", AppointmentStatus.SCHEDULED
        );
        view.appointmentAdded(existing);
        assertThat(model.getRowCount()).isEqualTo(1);

        // Act: call with empty list
        view.showAllAppointments(Collections.emptyList());

        // Assert: table is cleared
        assertThat(model.getRowCount()).isEqualTo(0);
    }

    @Test
    void showAllAppointments_withMultipleAppointments_populatesRows() {
        DefaultTableModel model = (DefaultTableModel) view.getTableModel();

        Appointment a1 = new Appointment(
            "1", "Alice",
            LocalDateTime.of(2025, 11, 10, 9, 0),
            "Cut", AppointmentStatus.SCHEDULED
        );
        Appointment a2 = new Appointment(
            "2", "Bob",
            LocalDateTime.of(2025, 11, 10, 10, 0),
            "Color", AppointmentStatus.COMPLETED
        );

        // Act
        view.showAllAppointments(List.of(a1, a2));

        // Assert
        assertThat(model.getRowCount()).isEqualTo(2);
        assertThat(model.getValueAt(0, 0)).isEqualTo("1");
        assertThat(model.getValueAt(0, 1)).isEqualTo("Alice");
        assertThat(model.getValueAt(1, 0)).isEqualTo("2");
        assertThat(model.getValueAt(1, 1)).isEqualTo("Bob");
    }

    @Test
    void appointmentAdded_onNonEmptyTable_appendsRow() {
        DefaultTableModel model = (DefaultTableModel) view.getTableModel();

        Appointment first = new Appointment(
            "1", "Alice",
            LocalDateTime.of(2025, 11, 10, 9, 0),
            "Cut", AppointmentStatus.SCHEDULED
        );
        Appointment second = new Appointment(
            "2", "Bob",
            LocalDateTime.of(2025, 11, 10, 10, 0),
            "Color", AppointmentStatus.SCHEDULED
        );

        // Arrange: table already has one row
        view.showAllAppointments(List.of(first));
        assertThat(model.getRowCount()).isEqualTo(1);

        // Act: add another appointment
        view.appointmentAdded(second);

        // Assert: second row is appended with correct data
        assertThat(model.getRowCount()).isEqualTo(2);
        assertThat(model.getValueAt(1, 0)).isEqualTo("2");
        assertThat(model.getValueAt(1, 1)).isEqualTo("Bob");
    }
    
    @Test
    void tableCellsAreNotEditable() {
    	
    	TableModel model = view.getTableModel();
    	
    	assertThat(model.isCellEditable(0, 0)).isFalse();
    }

}
