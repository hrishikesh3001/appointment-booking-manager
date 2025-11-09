package com.appointment.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    @Test
    void testAppointmentCreation() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            1L, 
            "John Doe", 
            date, 
            "Haircut", 
            AppointmentStatus.SCHEDULED
        );

        assertThat(appointment.getId()).isEqualTo(1L);
        assertThat(appointment.getCustomerName()).isEqualTo("John Doe");
        assertThat(appointment.getAppointmentDate()).isEqualTo(date);
        assertThat(appointment.getServiceType()).isEqualTo("Haircut");
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
    }

    @Test
    void testAppointmentWithNullCustomerName() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        
        assertThatThrownBy(() -> 
            new Appointment(1L, null, date, "Haircut", AppointmentStatus.SCHEDULED)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessage("Customer name cannot be null or empty");
    }

    @Test
    void testAppointmentWithEmptyCustomerName() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        
        assertThatThrownBy(() -> 
            new Appointment(1L, "", date, "Haircut", AppointmentStatus.SCHEDULED)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessage("Customer name cannot be null or empty");
    }

    @Test
    void testAppointmentWithNullDate() {
        assertThatThrownBy(() -> 
            new Appointment(1L, "John Doe", null, "Haircut", AppointmentStatus.SCHEDULED)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessage("Appointment date cannot be null");
    }

    @Test
    void testAppointmentWithNullServiceType() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        
        assertThatThrownBy(() -> 
            new Appointment(1L, "John Doe", date, null, AppointmentStatus.SCHEDULED)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessage("Service type cannot be null or empty");
    }

    @Test
    void testAppointmentWithEmptyServiceType() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        
        assertThatThrownBy(() -> 
            new Appointment(1L, "John Doe", date, "", AppointmentStatus.SCHEDULED)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessage("Service type cannot be null or empty");
    }

    @Test
    void testAppointmentWithNullStatus() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        
        assertThatThrownBy(() -> 
            new Appointment(1L, "John Doe", date, "Haircut", null)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessage("Status cannot be null");
    }

    @Test
    void testAppointmentEquality() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment1 = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );
        Appointment appointment2 = new Appointment(
            1L, "Jane Smith", date, "Massage", AppointmentStatus.COMPLETED
        );

        assertThat(appointment1).isEqualTo(appointment2);
        assertThat(appointment1.hashCode()).isEqualTo(appointment2.hashCode());
    }

    @Test
    void testAppointmentInequality() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment1 = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );
        Appointment appointment2 = new Appointment(
            2L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    void testAppointmentStatusEnum() {
        assertThat(AppointmentStatus.SCHEDULED).isNotNull();
        assertThat(AppointmentStatus.COMPLETED).isNotNull();
        assertThat(AppointmentStatus.CANCELED).isNotNull();
    }
    
    @Test
    void testAppointmentToString() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        String result = appointment.toString();

        assertThat(result).contains("id=1");
        assertThat(result).contains("customerName='John Doe'");
        assertThat(result).contains("serviceType='Haircut'");
        assertThat(result).contains("status=SCHEDULED");
    }

    @Test
    void testAppointmentHashCode() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment1 = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );
        Appointment appointment2 = new Appointment(
            1L, "Jane Smith", date, "Massage", AppointmentStatus.COMPLETED
        );

        assertThat(appointment1.hashCode()).isEqualTo(appointment2.hashCode());
    }

    @Test
    void testAppointmentEqualsWithNull() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        assertThat(appointment.equals(null)).isFalse();
    }

    @Test
    void testAppointmentEqualsWithDifferentClass() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        assertThat(appointment.equals("Not an Appointment")).isFalse();
    }

    @Test
    void testAppointmentEqualsWithSameObject() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appointment = new Appointment(
            1L, "John Doe", date, "Haircut", AppointmentStatus.SCHEDULED
        );

        assertThat(appointment.equals(appointment)).isTrue();
    }
}