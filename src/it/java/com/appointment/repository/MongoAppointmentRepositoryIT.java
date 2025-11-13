package com.appointment.repository;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

@Testcontainers
class MongoAppointmentRepositoryIT {

    @Container
    static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0");

    private MongoAppointmentRepository repository;

    @BeforeEach
    void setUp() {
        String uri = mongoContainer.getConnectionString();
        repository = new MongoAppointmentRepository(uri);
    }

    @Test
    void testSaveAndFind() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appt = new Appointment(null, "Alice", date, "Haircut", AppointmentStatus.SCHEDULED);

        Appointment saved = repository.save(appt);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isNotNull();
    }
}