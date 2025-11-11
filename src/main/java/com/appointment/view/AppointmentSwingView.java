package com.appointment.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

import com.appointment.model.Appointment;
import com.appointment.controller.BookingController;

public class AppointmentSwingView extends JFrame implements AppointmentView {

    private static final long serialVersionUID = 1L;

    private final JTextField clientNameTextBox = new JTextField(15);
    @SuppressWarnings("rawtypes")
    private final JComboBox serviceComboBox = new JComboBox<>(new String[]{"Haircut", "Color", "Massage"});
    private final JButton addButton = new JButton("Add");
    private final JButton deleteButton = new JButton("Delete Selected");
    private final JTable appointmentTable;
    private final DefaultTableModel tableModel;

    private BookingController controller;

    public AppointmentSwingView() {
        setTitle("Appointment Booking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 350);

        tableModel = new DefaultTableModel(new Object[]{"Id", "Client", "Service", "Date & Time", "Status"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        appointmentTable = new JTable(tableModel);

        JPanel north = new JPanel();
        north.add(new JLabel("Client:"));
        north.add(clientNameTextBox);
        north.add(new JLabel("Service:"));
        north.add(serviceComboBox);
        north.add(addButton);
        north.add(deleteButton);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        // names for AssertJ-Swing tests
        clientNameTextBox.setName("clientNameTextBox");
        serviceComboBox.setName("serviceComboBox");
        addButton.setName("addButton");
        deleteButton.setName("deleteButton");
        appointmentTable.setName("appointmentTable");

        // delegate actions to controller
        addButton.addActionListener(e -> {
            if (controller == null) return;
            String name = getClientName();
            if (name.isBlank()) {
                showError("Client name cannot be empty");
                return;
            }
            controller.newAppointment(name, LocalDateTime.now(), getService());
        });

        deleteButton.addActionListener(e -> {
            if (controller == null) return;
            int row = appointmentTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) tableModel.getValueAt(row, 0);
                controller.deleteAppointment(id);
            }
        });
    }

    public void setController(BookingController controller) { this.controller = controller; }

    public String getClientName() { return clientNameTextBox.getText().trim(); }

    public String getService() { return serviceComboBox.getSelectedItem().toString(); }

    // ---- AppointmentView ----
    @Override public void showAllAppointments(List<Appointment> appointments) {
        tableModel.setRowCount(0);
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{ a.getId(), a.getCustomerName(), a.getServiceType(), a.getAppointmentDate(), a.getStatus() });
        }
    }

    @Override public void appointmentAdded(Appointment a) {
        tableModel.addRow(new Object[]{ a.getId(), a.getCustomerName(), a.getServiceType(), a.getAppointmentDate(), a.getStatus() });
        clientNameTextBox.setText("");
    }

    @Override public void appointmentRemoved(String appointmentId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (appointmentId.equals(tableModel.getValueAt(i, 0))) {
                tableModel.removeRow(i);
                break;
            }
        }
    }

    @Override public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // for tests
    public DefaultTableModel getTableModel() { return tableModel; }
}
