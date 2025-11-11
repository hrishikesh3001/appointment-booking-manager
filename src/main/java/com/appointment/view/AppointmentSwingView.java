package com.appointment.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;

public class AppointmentSwingView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField clientNameTextBox = new JTextField(15);
    @SuppressWarnings("rawtypes")
    private JComboBox serviceComboBox = new JComboBox<>(new String[]{"Haircut", "Color", "Massage"});
    private JButton addButton = new JButton("Add");
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentSwingView() {
        setTitle("Appointment Booking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);

        tableModel = new DefaultTableModel(new Object[]{"Client", "Service", "Date & Time", "Status"}, 0);
        appointmentTable = new JTable(tableModel);

        JPanel north = new JPanel();
        north.add(new JLabel("Client:"));
        north.add(clientNameTextBox);
        north.add(new JLabel("Service:"));
        north.add(serviceComboBox);
        north.add(addButton);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        // set component names for AssertJ Swing
        clientNameTextBox.setName("clientNameTextBox");
        serviceComboBox.setName("serviceComboBox");
        addButton.setName("addButton");
        appointmentTable.setName("appointmentTable");

        // dummy controller hook – will be replaced by real controller later
        addButton.addActionListener(e -> addAppointment());
    }

    /* exposed for controller */
    public String getClientName() {
        return clientNameTextBox.getText().trim();
    }

    public String getService() {
        return serviceComboBox.getSelectedItem().toString();
    }

    public void addAppointment() {
        if (getClientName().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Client name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // build domain object – id generated later by service
        Appointment appt = new Appointment(null, getClientName(), LocalDateTime.now(), getService(), AppointmentStatus.SCHEDULED);
        tableModel.addRow(new Object[]{appt.getCustomerName(), appt.getServiceType(), appt.getAppointmentDate(), appt.getStatus()});
        clientNameTextBox.setText("");
    }

    /* for testing */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}