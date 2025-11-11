package com.appointment.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment {
	private final String id;
	private final String customerName;
	private final LocalDateTime appointmentDate;
	private final String serviceType;
	private final AppointmentStatus status;
	
	public Appointment(String id, String customerName, LocalDateTime appointmentDate, String serviceType, AppointmentStatus status) {
		
		if (customerName == null || customerName.trim().isEmpty()) {
			throw new IllegalArgumentException("Customer name cannot be null or empty");
		}
		if (appointmentDate == null) {
			throw new IllegalArgumentException("Appointment date cannot be null");
		}
		if (serviceType == null || serviceType.trim().isEmpty()) {
			throw new IllegalArgumentException("Service type cannot be null or empty");
		}
		if (status == null) {
			throw new IllegalArgumentException("Status cannot be null");
		}
		
		this.id =id;
		this.customerName = customerName;
		this.appointmentDate = appointmentDate;
		this.serviceType = serviceType;
		this.status = status;
		
	}
	
	public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;
    	if (o == null || getClass() != o.getClass()) return false;
    	Appointment that = (Appointment) o;
    	return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(id);
    }
    
    @Override
    public String toString() {
    	return "Appointment{" +
    			"id=" + id + 
    			", customerName='" + customerName + '\'' +
    			", appointmentDate=" + appointmentDate +
                ", serviceType='" + serviceType + '\'' +
                ", status=" + status +
                '}';
    }
}
